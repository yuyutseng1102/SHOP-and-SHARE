package com.chloe.buytogether.gather

enum class CategoryType(val positionOnSpinner: Int) {
    WOMAN(0),
    MAN(1),
    CHILD(2),
    SHOES_BAG(3),
    MAKEUP(4),
    HEALTH(5),
    FOOD(6),
    LIVING(7),
    APPLIANCE(8),
    PET(9),
    STATIONARY(10),
    SPORT(11),
    COMPUTER(12),
    TICKET(13),
    OTHER(14)
}


enum class CountryType(val positionOnSpinner: Int) {
    TAIWAN(0),
    JAPAN(1),
    KOREA(2),
    CHINA(3),
    USA(4),
    CANADA(5),
    EU(6),
    AUSTRALIA(7),
    SOUTH_EAST_ASIA(8),
    OTHER(9)
}

enum class ConditionType(val positionOnSpinner: Int) {
    BY_PRICE(0),
    BY_QUANTITY(1),
    BY_MEMBER(2)
}