package com.domino.smerp.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 907926224L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.domino.smerp.common.QBaseEntity _super = new com.domino.smerp.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath groupName1 = createString("groupName1");

    public final StringPath groupName2 = createString("groupName2");

    public final StringPath groupName3 = createString("groupName3");

    public final NumberPath<java.math.BigDecimal> inboundUnitPrice = createNumber("inboundUnitPrice", java.math.BigDecimal.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<com.domino.smerp.item.constants.ItemAct> itemAct = createEnum("itemAct", com.domino.smerp.item.constants.ItemAct.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final QItemStatus itemStatus;

    public final StringPath name = createString("name");

    public final NumberPath<java.math.BigDecimal> outboundUnitPrice = createNumber("outboundUnitPrice", java.math.BigDecimal.class);

    public final StringPath rfid = createString("rfid");

    public final NumberPath<java.math.BigDecimal> safetyStock = createNumber("safetyStock", java.math.BigDecimal.class);

    public final EnumPath<com.domino.smerp.item.constants.SafetyStockAct> safetyStockAct = createEnum("safetyStockAct", com.domino.smerp.item.constants.SafetyStockAct.class);

    public final StringPath specification = createString("specification");

    public final StringPath unit = createString("unit");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.itemStatus = inits.isInitialized("itemStatus") ? new QItemStatus(forProperty("itemStatus")) : null;
    }

}

