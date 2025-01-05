package com.e_commerce_creator.common.model;

import com.e_commerce_creator.common.model.users.Account;
import com.e_commerce_creator.common.util.SystemUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "audits")
@Data
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userId;
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    Account account;
    String takenAction;
    String responseCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a", timezone = "Africa/Cairo")
    Date timestamp;
    String details;
    String additionalDetails;
    String identifier;

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode tree = objectMapper.valueToTree(this);
        tree.remove("id");
        tree.remove("account");
        return SystemUtils.writeObjectAsString(tree);
    }

}
