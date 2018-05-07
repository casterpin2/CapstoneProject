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
        flex : 2,
        justifyContent: 'flex-start',
        alignItems: 'center',
        margin: 10
    },
    loginFBBtn : {
        width : (Dimensions.get('screen').width -100) /2 -10,
        borderRadius : 5,
        flexDirection : 'row',
        alignItems : 'stretch',
        justifyContent : 'flex-start',
        marginRight : 10,
        height : 50,
        flex: 1,
    },
    loginGGBtn : {
        width : (Dimensions.get('screen').width -100) /2 -10,
        borderRadius : 5,
        flexDirection : 'column',
        alignItems : 'center',
        justifyContent : 'center',
        height : 50,
    }, 
    errorMessage : {
        color: 'red',
        marginTop: 20,
    }, 
    imageItem : {
        flex :1, 
        width: null,
        height: null
    }, 
    GooglePlusStyle: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#dc4e41',
        height: 40,
        borderRadius: 5 ,
        margin: 5,
        width : 250,
     },
      
     FacebookStyle: {
       flexDirection: 'row',
       alignItems: 'center',
       backgroundColor: '#485a96',
       height: 40,
       borderRadius: 5 ,
       margin: 5,
       width : 250,
     },
      
     ImageIconStyle: {
        padding: 10,
        margin: 5,
        height: 25,
        width: 25,
        resizeMode : 'stretch',
      
     },
      
     TextStyle :{
      
       color: "#fff",
       marginBottom : 4,
       flexDirection : 'row',
       justifyContent : 'center',
       alignItems: 'center',
       
     },
      
     SeparatorLine :{
      
     backgroundColor : '#fff',
     width: 1,
     height: 40
      
     }
      
    
});

export default style;