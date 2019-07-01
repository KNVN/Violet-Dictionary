import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View
} from 'react-native';
import WidgetMenu from './WidgetMenu';
import Search from './Search';
import LanguageOption from './LanguageOption';
import WordList from './WordList';
import {createBottomTabNavigator,
  createStackNavigator,
} from 'react-navigation';
import Icon from 'react-native-vector-icons/Ionicons'

export const WidgetStack = createStackNavigator({
  Widget:  {
  screen :WidgetMenu,
  navigationOptions:{
    title: 'Widget Option'
  }},
});
export const SearchStack = createStackNavigator({
  Search: {
  screen :Search,
  navigationOptions:{
    title: 'Search',
    headerTitleStyle:{
      backgroundColor:'white',
    },
  }
  },
 Words: {
 screen :WordList,
 navigationOptions:{
   title: 'List'
 }
 },
});
export const LanguageStack = createStackNavigator({
  Language: {
    screen :LanguageOption,
    navigationOptions:{
    title: 'Language Setting'
  }
  },
});
export const Tabbar= createBottomTabNavigator({
    Widget: {
    screen :WidgetStack,
    navigationOptions:{
      tabBarLabel: 'Widget',
      tabBarIcon: ({tintColor}) =>(
        <Icon name="ios-switch" size={24} color={tintColor}/>
      )
    }
    },
    Search: {
    screen :SearchStack,
    navigationOptions:{
      tabBarLabel: 'Search',
      tabBarIcon: ({tintColor}) =>(
        <Icon name="ios-search" size={24} color={tintColor}/>
      )
    }
    },
    Language: {
    screen :LanguageStack,
    navigationOptions:{
      tabBarLabel: 'Language',
      tabBarIcon: ({tintColor}) =>(
        <Icon name="ios-settings" size={24} color={tintColor}/>
      )
    }
    },
},{
  tabBarOptions: {
      activeTintColor: '#9b59b6',
      inactiveTintColor: 'gray',
  },
});
