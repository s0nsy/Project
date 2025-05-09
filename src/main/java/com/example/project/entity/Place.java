package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String title;
   private String link;
   private String address;
   @Column(name="roadaddress")
   private String roadAddress;
   private String mapx;
   private String mapy;
   private String category;

   @ManyToOne(fetch= FetchType.LAZY)
   @JoinColumn(name="route_id")
   private Route route;

}
