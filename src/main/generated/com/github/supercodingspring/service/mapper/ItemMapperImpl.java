package com.github.supercodingspring.service.mapper;

import com.github.supercodingspring.repository.items.ItemEntity;
import com.github.supercodingspring.web.dto.items.Item;
import com.github.supercodingspring.web.dto.items.ItemBody;
import com.github.supercodingspring.web.dto.items.Spec;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-10T14:24:12+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16.1 (Microsoft)"
)
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item itemEntityToItem(ItemEntity itemEntity) {
        if ( itemEntity == null ) {
            return null;
        }

        Item item = new Item();

        item.setSpec( itemEntityToSpec( itemEntity ) );
        if ( itemEntity.getId() != null ) {
            item.setId( String.valueOf( itemEntity.getId() ) );
        }
        item.setName( itemEntity.getName() );
        item.setType( itemEntity.getType() );
        item.setPrice( itemEntity.getPrice() );

        return item;
    }

    @Override
    public ItemEntity idAndItemBodyToItemEntity(Integer id, ItemBody itemBody) {
        if ( id == null && itemBody == null ) {
            return null;
        }

        ItemEntity.ItemEntityBuilder itemEntity = ItemEntity.builder();

        if ( itemBody != null ) {
            itemEntity.cpu( itemBodySpecCpu( itemBody ) );
            itemEntity.capacity( itemBodySpecCapacity( itemBody ) );
            itemEntity.name( itemBody.getName() );
            itemEntity.type( itemBody.getType() );
            itemEntity.price( itemBody.getPrice() );
        }
        itemEntity.id( id );
        itemEntity.stock( 0 );

        return itemEntity.build();
    }

    protected Spec itemEntityToSpec(ItemEntity itemEntity) {
        if ( itemEntity == null ) {
            return null;
        }

        Spec spec = new Spec();

        spec.setCpu( itemEntity.getCpu() );
        spec.setCapacity( itemEntity.getCapacity() );

        return spec;
    }

    private String itemBodySpecCpu(ItemBody itemBody) {
        if ( itemBody == null ) {
            return null;
        }
        Spec spec = itemBody.getSpec();
        if ( spec == null ) {
            return null;
        }
        String cpu = spec.getCpu();
        if ( cpu == null ) {
            return null;
        }
        return cpu;
    }

    private String itemBodySpecCapacity(ItemBody itemBody) {
        if ( itemBody == null ) {
            return null;
        }
        Spec spec = itemBody.getSpec();
        if ( spec == null ) {
            return null;
        }
        String capacity = spec.getCapacity();
        if ( capacity == null ) {
            return null;
        }
        return capacity;
    }
}
