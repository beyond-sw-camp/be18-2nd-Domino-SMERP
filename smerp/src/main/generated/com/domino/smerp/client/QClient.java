package com.domino.smerp.client;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QClient is a Querydsl query type for Client
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClient extends EntityPathBase<Client> {

    private static final long serialVersionUID = -1697357808L;

    public static final QClient client = new QClient("client");

    public final StringPath address = createString("address");

    public final StringPath businessNumber = createString("businessNumber");

    public final StringPath ceoName = createString("ceoName");

    public final StringPath ceoPhone = createString("ceoPhone");

    public final NumberPath<Long> clientId = createNumber("clientId", Long.class);

    public final StringPath companyName = createString("companyName");

    public final StringPath job1st = createString("job1st");

    public final StringPath job2nd = createString("job2nd");

    public final StringPath job3rd = createString("job3rd");

    public final StringPath name1st = createString("name1st");

    public final StringPath name2nd = createString("name2nd");

    public final StringPath name3rd = createString("name3rd");

    public final StringPath phone = createString("phone");

    public final StringPath phone1st = createString("phone1st");

    public final StringPath phone2nd = createString("phone2nd");

    public final StringPath phone3rd = createString("phone3rd");

    public final EnumPath<com.domino.smerp.client.constants.TradeType> status = createEnum("status", com.domino.smerp.client.constants.TradeType.class);

    public final StringPath zipCode = createString("zipCode");

    public QClient(String variable) {
        super(Client.class, forVariable(variable));
    }

    public QClient(Path<? extends Client> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClient(PathMetadata metadata) {
        super(Client.class, metadata);
    }

}

