package com.example.WatchesStoreV2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private ObjectId id;

    private int star;

    private String content;

    private ObjectId product;

    private Date createdOn;

    private ObjectId user;
}
