package com.domino.smerp.warehouse;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QWarehouse is a Querydsl query type for Warehouse
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWarehouse extends EntityPathBase<Warehouse> {

    private static final long serialVersionUID = -541087654L;

    public static final QWarehouse warehouse = new QWarehouse("warehouse");

    public final com.domino.smerp.common.QBaseEntity _super = new com.domino.smerp.common.QBaseEntity(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final EnumPath<com.domino.smerp.warehouse.constants.DivisionType> divisionType = createEnum("divisionType", com.domino.smerp.warehouse.constants.DivisionType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final StringPath zipcode = createString("zipcode");

    public QWarehouse(String variable) {
        super(Warehouse.class, forVariable(variable));
    }

    public QWarehouse(Path<? extends Warehouse> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWarehouse(PathMetadata metadata) {
        super(Warehouse.class, metadata);
    }

}

