import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  TouchableOpacity, Picker
} from 'react-native';
var SharedPreferences = require('react-native-shared-preferences');

export default class LanguageOption extends Component {
  constructor(){
    super();
    this.state={
      Language1: "English",
      Language2: "English"
    }
  }
  clickMeSenpai(){
SharedPreferences.setItem("InputLanguage",this.state.Language1);
SharedPreferences.setItem("OutputLanguage",this.state.Language2);
  }
  render() {

    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{fontSize: 35, textAlign: 'center', color:'gray', marginTop: 10}}>OPTION</Text>
      <View style={{ height: 50, width: 130, marginTop: 70, borderWidth:1, borderColor:'gray', justifyContent: 'center', alignItems: 'center'}}>
      <Picker
      selectedValue={this.state.Language1}
      style={{ height: 50, width: 100}}
      onValueChange={(itemValue, itemIndex) => this.setState({Language1: itemValue})}>
      <Picker.Item label="English" value="Eng" />
      </Picker>
      </View>
      <View style={{ height: 50, width: 130, marginTop: 50, borderWidth:1, borderColor:'gray', justifyContent: 'center', alignItems: 'center'}}>
      <Picker
      selectedValue={this.state.Language2}
      style={{ height: 50, width: 100}}
      onValueChange={(itemValue, itemIndex) => this.setState({Language2: itemValue})}>
      <Picker.Item label="English" value="Eng" />
      </Picker>
      </View>



      <TouchableOpacity onPress={()=>{this.clickMeSenpai()}}>
        <View style={styles.Button}>
          <Text style={styles.text}> COMMIT </Text>
        </View>
      </TouchableOpacity>
      </View>
    );
  }
}
const styles = StyleSheet.create({

  text: {
    fontSize: 25,
    textAlign: 'center',
    margin: 10,
    color: 'white',
    fontWeight: 'bold'
  },
  Button: {
    width: 180,
    height: 60,
    justifyContent: 'center',
    alignItems: 'center',
	  marginTop: 50,
    backgroundColor: 'red',
    borderRadius:25,
    borderWidth: 4,
    borderColor: '#fff'
  },
});
