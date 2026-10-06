CREATE TABLE public.payment_operation (
    operation_id BIGINT GENERATED ALWAYS AS IDENTITY,
    payment_card_id BIGINT NOT NULL,
    tenant_id UUID NOT NULL,
    external_reference_id VARCHAR(100) NOT NULL,
    acquirer VARCHAR(50) NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_payment_operation
        PRIMARY KEY (operation_id),

    CONSTRAINT fk_payment_operation_payment_card
        FOREIGN KEY (payment_card_id)
        REFERENCES public.payment_card (payment_card_id),

    CONSTRAINT fk_payment_operation_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES public.tenant (id),

    CONSTRAINT uq_payment_operation_card_type
        UNIQUE (payment_card_id, operation_type)
);