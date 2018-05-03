import {StyleSheet, Dimensions} from 'react-native'

const style = StyleSheet.create({

    loginPage: {
        height: Dimensions.get('screen').height,
        justifyContent : 'center',
        alignItems: 'center',
        flexDirection : 'column',
        width : Dimensions.get('screen').width,
        flex : 1,
    },
    logo: {
        width :Dimensions.get('screen').width -100,
        justifyContent : 'center',
        alignItems: 'center',
        flex : 2,
    },
    loginForm : {
        width :Dimensions.get('screen').width -100,
        flexDirection : 'column',
        justifyContent : 'flex-start',
        alignItems: 'center',
        flex : 1.5,
        borderBottomColor : '#DDDDDD',
        borderBottomWidth : 1,
    },
    inputRow : {
        flexDirection : 'row',
        alignItems : 'center',
        justifyContent : 'center',
    },
    textInput : {
        width : Dimensions.get('screen').width - 150 ,
        height : 50,
        borderWidth : 1,
        borderRadius : 5,
        padding : 8,
        margin : 8,
        
    },
    icon : {
        width : 30,
    },
    buttonView : {
        alignItems : 'center',
        justifyContent : 'center',
    },
    loginBtn : {
        flexDirection : 'column',
        alignItems : 'center',
        justifyContent : 'center',
        width : 150,
        borderRadius : 5,
        
    },
    loginWithFBandGGForm : {
        width :Dimensions.get('screen').width -100,
        flexDirection : 'column',
        justifyContent : 'flex-start',
        alignItems: 'center',
        flex : 2,
        marginTop : 10
    },
    loginFBBtn : {
        backgroundColor : 'blue',
        width : 250,
        borderRadius : 5,
        flexDirection : 'column',
        alignItems : 'center',
        justifyContent : 'center',
        marginBottom : 10,
    },
    loginGGBtn : {
        backgroundColor : 'red',
        width : 250,
        borderRadius : 5,
        flexDirection : 'column',
        alignItems : 'center',
        justifyContent : 'center',
    }, 
    errorMessage : {
        color: 'red',
        marginTop: 20,
    }
});

export default style;