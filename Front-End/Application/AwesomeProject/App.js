import React from 'react'
import { StyleSheet, Text, View, StatusBar } from 'react-native'
// import Login from './src/components/Login/index'
// import SearchPage from './src/components/SearchPage/index'
// import ListDemo from './src/components/ListView/index'

// import MapView from './src/components/Maps/index'
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

