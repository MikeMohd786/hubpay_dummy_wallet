package com.hubpay.dummy_wallet.models;

import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPage {

    Integer page;
    Integer size;
    Integer totalPage;
    List<TransactionDTO> transactions;

}
