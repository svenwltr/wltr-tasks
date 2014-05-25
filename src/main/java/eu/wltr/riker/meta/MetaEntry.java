package eu.wltr.riker.meta;


import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_class")
public class MetaEntry {

}
