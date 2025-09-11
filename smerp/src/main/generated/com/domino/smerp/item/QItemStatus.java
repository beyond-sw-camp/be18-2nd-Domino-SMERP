package com.domino.smerp.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemStatus is a Querydsl query type for ItemStatus
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemStatus extends EntityPathBase<ItemStatus> {

    private static final long serialVersionUID = 169163490L;

    public static final QItemStatus itemStatus = new QItemStatus("itemStatus");

    public final NumberPath<Long> itemStatusId = createNumber("itemStatusId", Long.class);

    public final EnumPath<com.domino.smerp.item.constants.ItemStatusStatus> status = createEnum("status", com.domino.smerp.item.constants.ItemStatusStatus.class);

    public QItemStatus(String variable) {
        super(ItemStatus.class, forVariable(variable));
    }

    public QItemStatus(Path<? extends ItemStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemStatus(PathMetadata metadata) {
        super(ItemStatus.class, metadata);
    }

}

