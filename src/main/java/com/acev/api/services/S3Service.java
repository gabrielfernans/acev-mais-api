package com.acev.api.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

  private final AmazonS3 s3Client;

  @Value("${aws.s3.bucketName}")
  private String bucketName;

  public S3Service(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  public URL uploadFileToS3(MultipartFile file) {
    String key = UUID.randomUUID().toString(); // Gera uma chave única para a foto

    uploadFile(key, file);

    // Retorne o URL completo acessível ao público diretamente
    return getFileUrl(key);
  }

  public String uploadFile(String key, MultipartFile file) {
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());

      s3Client.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));

      return key;
    } catch (IOException e) {
      throw new RuntimeException("Erro ao fazer upload do arquivo para o S3", e);
    }
  }

  public URL getFileUrl(String key) {
    return s3Client.getUrl(bucketName, key);
  }

  public void deleteFile(String fileName) {
    s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
  }

  public String extractKeyFromUrl(String photoUrl) {
    String[] parts = photoUrl.split("/");
    return parts[parts.length - 1];
  }
}