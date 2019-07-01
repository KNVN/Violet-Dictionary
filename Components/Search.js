import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View, FlatList, TouchableOpacity
} from 'react-native';
import { SearchBar, List } from 'react-native-elements';

export default class Search extends Component {
  constructor() {
   super();
   this.state = {
       dataSource: ["Hi","Friend","How are you today?"],
       loaded: false,
       isSearching: false,
       feedURL: 'https://api.urbandictionary.com/v0/autocomplete?term='
   };
 }
searchForWord(word){
 let url = this.state.feedURL + word
        this.setState({isRefreshing: true})
fetch(url).then((response) => response.json()).then(response => {
                    this.setState({
                        dataSource: response,
                        loaded: true,
                        isRefreshing: false
                    })
                }).catch((error) => {
                    console.error(error);
                });


}
onSearch(term){
  this.props.navigation.navigate('Words', {
                search: term,
              });
}
 renderSeparator = () => {
     return (
       <View
         style={{
           height: 1,
           width: "86%",
           backgroundColor: "#CED0CE",
           marginLeft: "14%"
         }}
       />
     );
   };
   renderHeader = () => {
    return(
    <SearchBar
    placeholder="Search a word ..."
    onChangeText={(text) => this.searchForWord(text)}
    lightTheme round />
  );
  };
  _renderItem = ({item}) => (
                <TouchableOpacity underlayColor='white' style={{backgroundColor:'white', height:60}} onPress={() => this.onSearch(item)}>
                    <View style={styles.row}>
                        <Text style={styles.rowText}>{item}</Text>
                    </View>
                </TouchableOpacity>
            );
  render() {
    return (
//      <View style={{ flex: 1, justifyContent: 'center'}}>
    <List>
      <FlatList
  data={this.state.dataSource}
  renderItem={this._renderItem}
  ItemSeparatorComponent={this.renderSeparator}
  ListHeaderComponent={this.renderHeader}
       />
   </List>
//     </View>
    );
  }
}
const styles = StyleSheet.create({
    headerGroup:{
        flex:1,
        flexDirection:'row',
        marginTop:10,
        borderBottomColor: '#E8E8E8',
        borderBottomWidth: 1,


    },
    listContainer:{
        margin:20
    },
    row:{
        flexDirection:'row',
        justifyContent:'space-between',
        padding:5,
        borderBottomColor: '#E8E8E8',
        marginHorizontal:15,
        height:60
    },
    rowText:{
        fontSize: 20,
        textAlign: 'left',
        margin: 10,
        lineHeight:20,

    }

});
