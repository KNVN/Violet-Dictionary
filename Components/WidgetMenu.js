import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View, TouchableOpacity
} from 'react-native';
import {AndroidNavigator,Intent} from 'react-native-android-navigation';

export default class WidgetMenu extends Component {

  clickMeSenpai(){
    const intent = new Intent();
   intent.setClassName('com.dictionarylite.WidgetActivity');
   AndroidNavigator.startActivity(intent);
  }
  render() {
    return (
      <View style={{flex: 1, alignItems: 'center'}}>
       <Text style={styles.Header}> Mini app version, run anywhere </Text>
        <TouchableOpacity onPress={()=>{this.clickMeSenpai()}}>
          <View style={styles.Button}>
            <Text style={styles.text}> GO TO CENTER >> </Text>
          </View>
        </TouchableOpacity>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  Button: {
    width: 250,
    height: 100,
    justifyContent: 'center',
    alignItems: 'center',
	  alignSelf: 'center',
    backgroundColor: '#FFB266',
    borderRadius: 20,
    borderWidth: 5,
    borderColor: 'white',
    marginTop: 120
  },
  text: {
    fontSize: 30,
    textAlign: 'center',
    margin: 10,
    color: 'white',
    fontWeight: 'bold'
  },
  Header: {
    fontSize: 24,
    textAlign: 'center',
    color:'#9b59b6',
    fontStyle: 'italic',
    fontWeight:'500',
    marginTop: 50
  }
});
