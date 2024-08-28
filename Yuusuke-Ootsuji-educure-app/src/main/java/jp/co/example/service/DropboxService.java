package jp.co.example.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DropboxService {

	private final DbxClientV2 client;

    // Springによるアクセストークンのインジェクション
	@Autowired
    public DropboxService(@Value("${dropbox.access.token}") String accessToken) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("AgriConnect").build();
        this.client = new DbxClientV2(config, accessToken);
    }
    // テスト用コンストラクタ
    public DropboxService(DbxClientV2 client) {
        this.client = client;
    }

    public String uploadFile(MultipartFile file) throws IllegalArgumentException, IOException {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = "/" + fileName;
            // ファイルをアップロード
            FileMetadata metadata = client.files().uploadBuilder(filePath)
                    .uploadAndFinish(file.getInputStream());
            // 既存の共有リンクをチェック
            ListSharedLinksResult sharedLinks = client.sharing().listSharedLinksBuilder()
                    .withPath(filePath)
                    .withDirectOnly(true)
                    .start();
            if (!sharedLinks.getLinks().isEmpty()) {
                // 既存の共有リンクがある場合、それを返す
                SharedLinkMetadata existingLink = sharedLinks.getLinks().get(0);
                return existingLink.getUrl();
            }
            // 共有リンクがない場合、新しいリンクを作成
            SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(filePath);
            return sharedLinkMetadata.getUrl();
        } catch (DbxException e) {
            if (e.getMessage().contains("file size")) {
                throw new IllegalArgumentException("File size exceeds the maximum allowed size", e);
            }
            throw new IOException("Error uploading file to Dropbox", e);
        }
    }
}