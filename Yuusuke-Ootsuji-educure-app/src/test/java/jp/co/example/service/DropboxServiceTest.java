package jp.co.example.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.sharing.DbxUserSharingRequests;
import com.dropbox.core.v2.sharing.ListSharedLinksBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class DropboxServiceTest {

    private DropboxService dropboxService;

    @Mock
    private DbxClientV2 dbxClientV2;

    @Mock
    private DbxUserFilesRequests filesMock;

    @Mock
    private UploadBuilder uploadBuilderMock;

    @Mock
    private DbxUserSharingRequests sharingMock;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private FileMetadata fileMetadata;

    @Mock
    private ListSharedLinksResult listSharedLinksResult;

    @Mock
    private SharedLinkMetadata sharedLinkMetadata;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private ListSharedLinksBuilder listSharedLinksBuilderMock;

    @BeforeEach
    public void setUp() throws IOException, DbxException {
        // DropboxServiceのインスタンスをモックしたDbxClientV2で作成
        dropboxService = new DropboxService(dbxClientV2);

        // すべてのテストで共通して使用される設定のみを行う
        doReturn("testimage.jpg").when(multipartFile).getOriginalFilename();
        doReturn(mockInputStream).when(multipartFile).getInputStream();

        doReturn(filesMock).when(dbxClientV2).files();
        doReturn(uploadBuilderMock).when(filesMock).uploadBuilder(any(String.class));
    }

    @Test
    public void uploadFile_正常にファイルをアップロードできる場合() throws IOException, DbxException {
        // 追加のモック設定（このテストでのみ必要）
        doReturn(sharedLinkMetadata).when(sharingMock).createSharedLinkWithSettings(any(String.class));
        doReturn("http://dropbox.test/link").when(sharedLinkMetadata).getUrl();
        doReturn(sharingMock).when(dbxClientV2).sharing();
        doReturn(listSharedLinksBuilderMock).when(sharingMock).listSharedLinksBuilder();
        doReturn(listSharedLinksBuilderMock).when(listSharedLinksBuilderMock).withPath(any(String.class));
        doReturn(listSharedLinksBuilderMock).when(listSharedLinksBuilderMock).withDirectOnly(true);
        doReturn(listSharedLinksResult).when(listSharedLinksBuilderMock).start();
        doReturn(Collections.emptyList()).when(listSharedLinksResult).getLinks();

        String result = dropboxService.uploadFile(multipartFile);

        assertThat(result).isEqualTo("http://dropbox.test/link");
    }

    @Test
    public void uploadFile_既存の共有リンクがある場合() throws IOException, DbxException {
        // 追加のモック設定（このテストでのみ必要）
        doReturn(Collections.singletonList(sharedLinkMetadata)).when(listSharedLinksResult).getLinks();
        doReturn("http://dropbox.test/existing-link").when(sharedLinkMetadata).getUrl();
        doReturn(sharingMock).when(dbxClientV2).sharing();
        doReturn(listSharedLinksBuilderMock).when(sharingMock).listSharedLinksBuilder();
        doReturn(listSharedLinksBuilderMock).when(listSharedLinksBuilderMock).withPath(any(String.class));
        doReturn(listSharedLinksBuilderMock).when(listSharedLinksBuilderMock).withDirectOnly(true);
        doReturn(listSharedLinksResult).when(listSharedLinksBuilderMock).start();

        String result = dropboxService.uploadFile(multipartFile);

        assertThat(result).isEqualTo("http://dropbox.test/existing-link");
    }

    @Test
    public void uploadFile_ファイルサイズエラーの場合() throws IOException, DbxException {
        // このテストでのみ必要なモック設定
        doThrow(new DbxException("file size")).when(uploadBuilderMock)
                .uploadAndFinish(any(InputStream.class));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dropboxService.uploadFile(multipartFile);
        });

        assertThat(exception.getMessage()).contains("File size exceeds the maximum allowed size");
    }

    @Test
    public void uploadFile_その他のDropboxエラーの場合() throws IOException, DbxException {
        // このテストでのみ必要なモック設定
        doThrow(new DbxException("general error")).when(uploadBuilderMock)
                .uploadAndFinish(any(InputStream.class));

        IOException exception = assertThrows(IOException.class, () -> {
            dropboxService.uploadFile(multipartFile);
        });

        assertThat(exception.getMessage()).contains("Error uploading file to Dropbox");
    }
}
