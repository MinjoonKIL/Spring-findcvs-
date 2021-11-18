import {AttachMoney, BorderColor, DeliveryDining, Fastfood, LocalCafe, MoreTime} from "@mui/icons-material";
import {createContext, } from "react";

export const DefaultMenuItems = [
    {
        name: '24시간 영업',
        servicesProvided : 'ALL_DAY',
        Icon: MoreTime,
        selected : false
    },
    {
        name: '택배',
        servicesProvided : 'DELIVERY',
        Icon: DeliveryDining,
        selected : false
    },
    {
        name: '즉석튀김류',
        servicesProvided : 'FRIED',
        Icon: Fastfood,
        selected : false
    },
    {
        name: '카페',
        servicesProvided : 'CAFE',
        Icon: LocalCafe,
        selected : false
    },
    {
        name: '로또&토토',
        servicesProvided : 'LOTTERY',
        Icon: BorderColor,
        selected : false
    },
    {
        name: '현금인출기',
        servicesProvided : 'ATM',
        Icon: AttachMoney,
        selected : false
    }
];

export const MenuItemsContext = createContext({
    menuItems : [],
    updateSelected : () => {}
})
