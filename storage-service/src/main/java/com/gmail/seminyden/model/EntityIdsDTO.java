package com.gmail.seminyden.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class EntityIdsDTO {

    private List<Long> ids;
}