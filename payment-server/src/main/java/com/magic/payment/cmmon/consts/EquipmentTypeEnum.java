package com.magic.payment.cmmon.consts;

public enum EquipmentTypeEnum {
    ANDROID(1,"安卓"),
    IOS(2,"IOS"),
    OTHER(3,"其他");
    public final Integer code;
    public final String equipmentType;

    EquipmentTypeEnum(Integer code, String equipmentType) {
        this.code = code;
        this.equipmentType = equipmentType;
    }
}
