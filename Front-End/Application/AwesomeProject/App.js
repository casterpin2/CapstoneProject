import React from 'react'
import { StyleSheet, Text, View, StatusBar } from 'react-native'
import AppRouter from './src/AppRouter'


export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <AppRouter />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    marginTop : StatusBar.currentHeight,
  },
});

