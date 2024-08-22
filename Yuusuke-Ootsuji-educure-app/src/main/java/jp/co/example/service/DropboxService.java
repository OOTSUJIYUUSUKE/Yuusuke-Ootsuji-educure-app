package jp.co.example.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DropboxService {

	@Value("${dropbox.access.token}")
	private String accessToken;

	public String uploadFile(MultipartFile file) throws IOException {
		DbxRequestConfig config = DbxRequestConfig.newBuilder("AgriConnect").build();
		DbxClientV2 client = new DbxClientV2(config, accessToken);

		try {
            String fileName = file.getOriginalFilename();
            String filePath = "/" + fileName;

            
            FileMetadata metadata = client.files().uploadBuilder(filePath)
                    .uploadAndFinish(file.getInputStream());

            // アップロード後に共有リンクを作成
            SharedLinkMetadata sharedLinkMetadata = client.sharing().createSharedLinkWithSettings(filePath);
            String url = sharedLinkMetadata.getUrl();
            return replaceDownloadParameter(url);

        } catch (DbxException e) {
			if (e.getMessage().contains("file size")) {
				throw new IOException("File size exceeds the maximum allowed size", e);
			}
			throw new IOException("Error uploading file to Dropbox", e);
		}
	}

	private String replaceDownloadParameter(String url) {
		if (url.contains("?dl=0")) {
			return url.replace("?dl=0", "?dl=1");
		} else if (url.contains("&dl=0")) {
			return url.replace("&dl=0", "&dl=1");
		}
		return url;
	}
}
