import {StyleSheet, Dimensions} from 'react-native'

const deviceWidth = Dimensions.get('window').width

const styles = StyleSheet.create({
    container: {
      flex: 1,
      alignItems: 'center',
      justifyContent: 'center',
      marginTop: 5,
    },
    barContainer: {
      position: 'relative',
      zIndex: 2,
      flexDirection: 'row',
      alignItems : 'center',
      justifyContent: 'center',
      bottom : 20,
      width: deviceWidth - 142,
    },
    track: {
      backgroundColor: '#ccc',
      overflow: 'hidden',
      height: 10,
      width :10,
      borderRadius: 10,
    },
    bar: {
      backgroundColor: '#5294d6',
      height: 10,
      width :10,
      position: 'absolute',
      left: 0,
      top: 0,
      borderRadius: 10,
    },
    imageDisplay : {
      width: deviceWidth, 
      height: 250
    }
  })
  export default styles;