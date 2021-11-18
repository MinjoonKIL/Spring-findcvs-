/*global kakao*/
import React, {useContext, useEffect, useState} from 'react'
import {Button, Card, CardMedia, CardContent, Typography, CardActions, StepIcon} from "@mui/material";
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import ToggleButton from '@mui/material/ToggleButton';
import FormatAlignCenterIcon from '@mui/icons-material/FormatAlignCenter';
import {Map, MapMarker} from "react-kakao-maps-sdk";
import {GS25Icon, CuIcon} from "./CVSIcons"
import SEIcon from "./images/SE_lcon.png"
import EmartIcon from "./images/emart24_logo.png"
import CuMarker from "./images/cu_marker.png"
import SeMarker from "./images/se_marker.png"
import EmartMarker from "./images/emart_marker.png"
import GSMarker from "./images/gs_marker.png"
import MYMarker from "./images/me_marker.png"
import CU_SVC from "./images/cu_cvs.png"
import s7_SVC from "./images/7E_cvs.jpg"
import Emart_SVC from "./images/emart_cvs.jpg"
import GS25_SVC from "./images/gs25_cvs.jpg"
import styles from "./Location.module.css";

import axios from "axios";
import zIndex from "@mui/material/styles/zIndex";
import {MenuItemsContext} from "./MenuItems-context";


const instance = axios.create({
    baseURL: process.env.REACT_APP_API_URL
});


const Location = () => {
    const menuItems = useContext(MenuItemsContext).menuItems;
    const [brand , updateBrand] = useState([]);

    const handleBrand = (event, newBrands) => {
        updateBrand(newBrands);
        //updateMarker(map.getCenter().getLat(), map.getCenter().getLng());
    }

    //편의점 아이콘
    const CVSFilter = () => (
            <ToggleButtonGroup className={styles.mobile}
                    size="large"
                    value={brand}
                    onChange={handleBrand}
                    sx={{
                        position: "absolute",
                        overflow: "hidden",
                        top: "90px",
                        left: "100px",
                        zIndex: 10,
                        backgroundColor: "#fff"
                    }}>
                <ToggleButton value="CU">
                    <CuIcon/>
                </ToggleButton>
                <ToggleButton value="GS25"  >
                    <GS25Icon/>
                </ToggleButton>
                <ToggleButton value="SEVEN_ELEVEN" >
                    <img src={SEIcon} width={30} height={30}/>
                </ToggleButton>
                <ToggleButton value="EMART24" >
                    <img src={EmartIcon} width={28} height={28}/>
                </ToggleButton>
            </ToggleButtonGroup>

    );


    //지도 메타 정보
    const [state, setState] = useState({
        // 지도의 초기 위치 (호서전문학교)
        center: {lat: 37.56290925619275, lng: 126.8399490701588},
        // 지도 위치 변경시 panto를 이용할지에 대해서 정의
        isPanto: false,
    });

    const markerIcons = {
        "CU": CuMarker,
        "GS25": GSMarker,
        "SEVEN_ELEVEN": SeMarker,
        "EMART24": EmartMarker,
        "MY": MYMarker
    }

    const markercvs = {
        "CU": CU_SVC,
        "GS25": GS25_SVC,
        "SEVEN_ELEVEN": s7_SVC,
        "EMART24": Emart_SVC,
        "MY": CU_SVC
    }

    //인포윈도우 Open 여부를 저장하는 state 입니다.
    const [isOpen, setIsOpen] = useState(false)

    //마커 스테이트
    const [markers, setMarkers] = useState([]);


    //위치 권한
    function updateMarker(lat, lng) {
        instance.get('api/', {
            params: {
                c: lat + "," + lng,
                servicesProvideds: menuItems.filter(i => i.selected).map(i => i.servicesProvided).join(","),
                brands: brand.join(",")
            }
        }).then((rep) => {
            setMarkers(rep.data);
        });
    }

    //위치 권한 정보X
    useEffect(() => {
        navigator.geolocation.getCurrentPosition((position) => {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;
            setState({
                center: {lat: lat, lng: lng},
                isPanto: true,
            });
            updateMarker(lat, lng);
        }, () => {
            alert("위치정보를 사용할수 없습니다.");
            updateMarker(37.56290925619275, 126.8399490701588);
        });
    }, [])


    //편의점 정보 card
    function MediaCard(props) {
        return (
                <Card sx={{maxWidth: 345, minWidth: 300}} className={styles.mobile_card}>
                    <CardMedia
                            component="img"
                            height="150"
                            image={markercvs[props.info.brand]}
                            alt="green iguana"
                    />
                    <div>
                    <CardContent className={styles.text}>
                        <Typography gutterBottom variant="h6" component="text.secondary">
                            {props.info.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            {props.info.telno}
                        </Typography>
                        <br/>
                        <Typography variant="body1" color="text.secondary">
                            {props.info.address}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            {props.info.servicesProvideds.toString()
                                    .replaceAll("ALL_DAY", "24시간")
                                    .replaceAll("DELIVERY", "택배")
                                    .replaceAll("FRIED", "튀김류")
                                    .replaceAll("CAFE", "카페")
                                    .replaceAll("LOTTERY", "로또&토토")
                            }
                        </Typography>
                        <div onClick={() => setIsOpen(false)}>
                            <Button className={styles.button1}>close</Button></div>
                        <br/>
                    </CardContent>
                    </div>
                </Card>
        );
    }

    const CVSMarker = (props) => (
            <MapMarker
                    key={`${props.info.id}`}
                    zIndex={-1}
                    position={{lat: props.info.location.lat, lng: props.info.location.lon}}// 마커를 표시할 위치
                    image={{
                        src: markerIcons[props.info.brand], // 마커이미지의 주소입니다
                        size: {
                            widht: 24,
                            height: 35,
                        }, // 마커이미지의 크기입니다

                    }}
                    clickable={true} // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
                    onClick={   //마커를 클릭하면 정보가 표시됩니다.
                        () => setIsOpen(props.info.id)
                    }
            >
                {isOpen == props.info.id && <MediaCard info={props.info}/>}
            </MapMarker>


    );


    //참고 URL
    // https://velog.io/@kwonh/React-CSS%EB%A5%BC-%EC%9E%91%EC%84%B1%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95%EB%93%A4-css-module-sass-css-in-js
    // https://react-kakao-maps-sdk.jaeseokim.dev/
    return (
            <>
                <Map center={state.center}
                     isPanto={state.isPanto}
                     onDragStart={(map) => setIsOpen(false)}
                     style={{
                         // 지도의 크기
                         width: "100%",
                         height: '95vh',
                     }}
                     level={3} // 지도의 확대 레벨
                     onCenterChanged={(map) => {
                         updateMarker(map.getCenter().getLat(), map.getCenter().getLng());
                     }}

                >
                    <MapMarker position={state.center}
                               image={{
                                   src: MYMarker, // 마커이미지의 주소입니다
                                   size: {
                                       widht: 30,
                                       height: 41
                                   } // 마커이미지의 크기입니다
                               }}
                               clickable={true} // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
                    >
                    </MapMarker>

                    {
                        markers.map((info, index) => (
                                <CVSMarker info={info} key={'marker_' + index}/>
                        ))
                    }
                </Map>
                <CVSFilter/>
            </>
    )
}
export default Location;
