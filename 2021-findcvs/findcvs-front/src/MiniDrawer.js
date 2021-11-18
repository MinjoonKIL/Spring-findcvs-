import * as React from 'react';
import './MiniDrawer.css';
import {styled, useTheme} from '@mui/material/styles';
import Box from '@mui/material/Box';
import MuiDrawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import CssBaseline from '@mui/material/CssBaseline';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import PageviewIcon from '@mui/icons-material/Pageview'
import {indigo, blue} from '@mui/material/colors';
import Location from "./component/Location";
import {MoreTime, DeliveryDining, Fastfood, LocalCafe, BorderColor, AttachMoney, Login} from '@mui/icons-material';
import {alpha, Avatar, Button, Icon, InputBase, MenuList, TextField} from "@mui/material";
import {useContext, useState} from "react";
import {MenuItemsContext, DefaultMenuItems } from "./component/MenuItems-context"
import styles from "./MiniDrawer.css";

const drawerWidth = 240;

const openedMixin = (theme) => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin = (theme) => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(7)} + 1px)`,
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(9)} + 1px)`,
    },
});

const openedMixin2 = (theme) => ({
    width: drawerWidth,
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    overflowX: 'hidden',
});

const closedMixin2 = (theme) => ({
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    width: `calc(${theme.spacing(0)} + 1px)`,  //이거 바꿔야 없어짐
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(1)} + 1px)`,
    },
});


const DrawerHeader = styled('div')(({theme}) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({theme, open}) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

const Drawer = styled(MuiDrawer, {shouldForwardProp: (prop) => prop !== 'open'})(
        ({theme, open}) => ({
            width: drawerWidth,
            flexShrink: 0,
            whiteSpace: 'nowrap',
            boxSizing: 'border-box',
            ...(open && {
                ...openedMixin(theme),
                '& .MuiDrawer-paper': openedMixin(theme),
            }),
            ...(!open && {
                ...closedMixin(theme),
                '& .MuiDrawer-paper': closedMixin(theme),
            }),
        }),
);

const Drawer2 = styled(MuiDrawer, {shouldForwardProp: (prop) => prop !== 'open'})(
        ({theme, open}) => ({
            width: drawerWidth,
            flexShrink: 0,
            whiteSpace: 'nowrap',
            boxSizing: 'border-box',
            ...(open && {
                ...openedMixin2(theme),
                '& .MuiDrawer-paper': openedMixin2(theme),
            }),
            ...(!open && {
                ...closedMixin2(theme),
                '& .MuiDrawer-paper': closedMixin2(theme),
            }),
        }),
);


function MenuItems() {
    return (
            <MenuItemsContext.Consumer>
                {({menuItems, updateSelected}) => {
                    const onClick = index => () => {
                        const item = menuItems[index];
                        const newItems = [...menuItems];
                        newItems[index] = {...item, selected: !item.selected};
                        updateSelected(newItems);
                    };

                    return <List>
                        {menuItems.map((item, index) =>
                                <ListItem
                                        className="ListItem"
                                        key={'menu' + index}
                                        button
                                        selected={item.selected}
                                        onClick={onClick(index)}
                                >
                                    <ListItemIcon>

                                        <Avatar>
                                            <item.Icon/>
                                        </Avatar>
                                    </ListItemIcon>
                                    <ListItemText primary={item.name}/>
                                </ListItem>
                        )}
                    </List>
                }}
            </MenuItemsContext.Consumer>
    );
}


const Search = styled('div')(({theme}) => ({
    position: 'relative',
    borderRadius: theme.shape.borderRadius,
    backgroundColor: alpha(theme.palette.common.white, 0.15),
    '&:hover': {
        backgroundColor: alpha(theme.palette.common.white, 0.25),
    },
    marginRight: theme.spacing(2),
    marginLeft: 0,
    width: '100%',
    [theme.breakpoints.up('sm')]: {
        marginLeft: theme.spacing(3),
        width: 'auto',
    },
}));

const SearchIconWrapper = styled('div')(({theme}) => ({
    padding: theme.spacing(0, 2),
    height: '100%',
    position: 'absolute',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
}));

const StyledInputBase = styled(InputBase)(({theme}) => ({
    color: 'inherit',
    '& .MuiInputBase-input': {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)})`,
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: '20ch',
        },
    },
}));


export default function MiniDrawer() {
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const [menus, updateMenuItems] = useState(DefaultMenuItems);
    let state = {
        menuItems : menus,
        updateSelected : updateMenuItems
    }

    return (

            <MenuItemsContext.Provider value={state}>
                <Box sx={{display: 'flex'}}>
                    <CssBaseline/>
                    <AppBar position="fixed" open={open}>
                        <Toolbar>
                            <IconButton
                                    color="inherit"
                                    aria-label="open drawer"
                                    onClick={handleDrawerOpen}
                                    edge="start"
                                    sx={{
                                        marginRight: '36px',
                                        ...(open && {display: 'none'}),
                                    }}
                            >
                                <MenuIcon/>
                            </IconButton>


                            <Search>
                                <SearchIconWrapper>
                                    <SearchIcon/>
                                </SearchIconWrapper>
                                <StyledInputBase
                                        placeholder="편의점 이름, 장소 ..."
                                        inputProps={{'aria-label': 'search'}}
                                />
                            </Search>
                            <Box sx={{flexGrow: 1}}/>
                            <Box sx={{display: "flex"}}>
                                <Button color="inherit" startIcon={<Login/>} ><span className="bar">로그인</span></Button>
                            </Box>

                        </Toolbar>
                    </AppBar>

              <div className="pc">
                    <Drawer variant="permanent" open={open}>
                        <DrawerHeader>
                            <PageviewIcon sx={{color: blue[200], mr: 1, fontSize: 40}}/>
                            <Typography variant="h6" noWrap component="div">
                                찾아줘! 편의점
                            </Typography>
                            <IconButton onClick={handleDrawerClose}>
                                {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                            </IconButton>
                        </DrawerHeader>
                        <Divider/>
                        <List>
                            <MenuItems/>
                        </List>
                    </Drawer>
                    </div>


                    <div className="mobile">
                        <Drawer2 variant="permanent" open={open}>
                            <DrawerHeader>
                                <PageviewIcon sx={{color: blue[200], mr: 1, fontSize: 40}}/>
                                <Typography variant="h6" noWrap component="div">
                                    찾아줘! 편의점
                                </Typography>
                                <IconButton onClick={handleDrawerClose}>
                                    {theme.direction === 'rtl' ? <ChevronRightIcon/> : <ChevronLeftIcon/>}
                                </IconButton>
                            </DrawerHeader>
                            <Divider/>
                            <List>
                                <MenuItems/>
                            </List>
                        </Drawer2>
                    </div>

                    <Box component="main" sx={{flexGrow: 1}}>
                        <DrawerHeader/>
                        <Location/>
                    </Box>
                </Box>

            </MenuItemsContext.Provider>

    );
}
