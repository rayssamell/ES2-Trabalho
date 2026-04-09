
    create table Item (
        qtde integer not null,
        valorItem float(53) not null,
        codigoItem bigserial not null,
        pedido_id bigint,
        produto_id bigint,
        primary key (codigoItem)
    );

    create table Pedido (
        data date,
        valorTotal float(53) not null,
        id bigserial not null,
        primary key (id)
    );

    create table PRODUTO (
        dataValidade date,
        estoque integer not null,
        preco float(53) not null,
        id bigserial not null,
        tipo varchar(31) not null,
        nome varchar(255),
        voltagem varchar(255),
        primary key (id)
    );

    alter table if exists Item 
       add constraint FKsma26iy8jalymbjk0in2dg8dc 
       foreign key (pedido_id) 
       references Pedido;

    alter table if exists Item 
       add constraint FK4fp4nigvmkd49ycfaeqd9ybt5 
       foreign key (produto_id) 
       references PRODUTO;
