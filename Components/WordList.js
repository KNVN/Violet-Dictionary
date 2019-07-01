import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View, FlatList
} from 'react-native';
import { List } from 'react-native-elements';

export default class WordList extends Component {
  constructor() {
   super();
   this.state = {
       dataSource: [{word: "Require Internet Connection"},{definition:"Wordlist"},{example: "Oh no"}, {author:"Network Connection Issue"}],
       feedURL: 'https://api.urbandictionary.com/v0/define?term='
   };
 }
 _renderItem = ({item}) => (
   <View style={ styles.container }>
   <Text style={ styles.title }>{item.word} </Text>
   <Text style={ styles.definition }> {item.definition}</Text>
   <Text style={ styles.example }> {item.example}</Text>
   <Text style={ styles.author}>Author: {item.author} </Text>
   </View>
           );
  render() {
     const { navigation } = this.props;
    const param= navigation.getParam('search', '');

  let url = this.state.feedURL + param;
  fetch(url).then((response) => response.json()).then(response => {
                       this.setState({
                           dataSource: response.list,
                           loaded: true,
                           isRefreshing: false
                       })
                   }).catch((error) => {
                       console.error(error);
                   });

    return (

      <List>
        <FlatList
    data={this.state.dataSource}
    renderItem={this._renderItem}

         />
     </List>
    );
  }
}

const styles = StyleSheet.create({
    container: {
        margin:20,
        padding:10,
        shadowColor: "#000000",
        shadowOpacity: 0.2,
        shadowRadius: 3,
        shadowOffset: {
        height: 5,
        width: 5
        },
        borderRadius: 4,
        alignItems: 'stretch',
        justifyContent: 'center',
        flex: 1,
        backgroundColor: '#ffffff',
        overflow:'visible'
    },
    title: {
        fontSize: 25,
        fontWeight: 'bold',
        fontFamily: 'Arial Rounded MT Bold',
        textAlign: 'left',
        color: '#9b59b6',
        paddingBottom:20,

    },
    definition: {
        fontSize: 18,
        fontWeight: '400',
        fontFamily: 'sans-serif',
        textAlign: 'left',
        color: '#4A4A4A',
        paddingBottom:20,

    },
    example:{
        fontSize: 16,
        fontStyle:'italic',
        fontWeight: '100',
        textAlign: 'left',
        color: '#23282C',
        paddingBottom:20,
    },
    author:{
        fontSize: 18,
        fontWeight: '600',
        textAlign: 'left',
        color: '#bdc3c7',
        paddingBottom:10,
    },



});
