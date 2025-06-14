package com.example.project.service;

import com.example.project.entity.Access;
import com.example.project.entity.Image;
import com.example.project.entity.dto.AccessRequest;
import com.example.project.entity.dto.ImageDto;
import com.example.project.mapper.AccessMapper;
import com.example.project.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {

   private final AccessMapper accessMapper;
   private final UserMapper userMapper;

   public void createAccess(AccessRequest request,String username){
      Access access = new Access();
      access.setTitle(request.getTitle());
      access.setContent(request.getContent());
      List<Image> images= new ArrayList<>();
      for(ImageDto i : request.getImageUrl()){
         Image image = new Image();
         image.setImageUrl(i.getImageUrl());
         image.setAccess(access);
         images.add(image);
      }
      access.setImageUrl(images);
      access.setCreated_at(LocalDateTime.now());
      access.setWriter(userMapper.findByUsername(username));
      accessMapper.save(access);
      for(ImageDto image: request.getImageUrl()) {
         Image newImage = new Image();
         newImage.setImageUrl(image.getImageUrl());
         newImage.setAccess(access);
         accessMapper.saveImage(newImage,access);
      }
   }

   public void editAccess(AccessRequest request, Long accessId, String username) throws AccessDeniedException {
      Access access = accessMapper.findByAccessId(accessId);
      System.out.println(access);
      Long userId = accessMapper.findUserIdByAccessId(accessId);
      if(access==null)
         throw new NullPointerException("해당 게시글이 없습니다.");
      else if(userId!=userMapper.findByUsername(username).getId())
         throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다.");
      access.setTitle(request.getTitle());
      access.setContent(request.getContent());
      access.setUpdated_at(LocalDateTime.now());
      accessMapper.editAccess(access);
      for(ImageDto image: request.getImageUrl()) {
         Image newImage = new Image();
         newImage.setImageUrl(image.getImageUrl());
         newImage.setAccess(access);
         accessMapper.saveImage(newImage,access);
      }
   }

   public void deleteAccess(Long id, String username) throws AccessDeniedException {
      Access access = accessMapper.findByAccessId(id);
      Long userId = accessMapper.findUserIdByAccessId(id);
      if(access==null)
         throw new NullPointerException("해당 게시글이 없습니다.");
      else if(userId!=userMapper.findByUsername(username).getId())
         throw new AccessDeniedException("해당 게시글의 작성자가 아닙니다.");
      accessMapper.deleteImageByAccessId(access.getId());

      accessMapper.deleteAccess(access);

   }
}
