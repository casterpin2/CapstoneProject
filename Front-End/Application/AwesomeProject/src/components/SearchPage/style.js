import {StyleSheet, Dimensions} from 'react-native'

const style  = StyleSheet.create({
    header : {
        width : Dimensions.get('screen').width,
        backgroundColor: '#6495ED'
        
    }, 
    headerLeft : {
        justifyContent : 'flex-start',
        alignItems: 'flex-start',
        width : 20,
    },
    headerRight : {
        justifyContent : 'flex-end',
        alignItems: 'flex-end',
        width : 20,
    }
});

export default style ;