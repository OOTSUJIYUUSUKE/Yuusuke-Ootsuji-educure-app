package jp.co.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NotEmptyMultipartFilesValidator implements ConstraintValidator<NotEmptyMultipartFiles, List<MultipartFile>> {
    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        // ファイルリストが空でないかどうかをチェック
        return files != null && !files.isEmpty() && files.stream().anyMatch(file -> !file.isEmpty());
    }
}
