import {StyleSheet, Dimensions} from 'react-native'

const style = StyleSheet.create({
    category: {
        marginTop : 10,
        backgroundColor : '#CCCCCC',
        height: 130,
    },
    mainCategory : {
        marginLeft: 10,
        marginBottom: 5,
        marginTop: 5,
        height : 130,
    },
    itemInScroolView : {
        borderWidth : 1,
        borderColor : 'red',
        borderRadius: 5,
        height : 80,
        width : 80,
        flex: 1,
        flexDirection: "row",
        alignItems: "stretch",
        backgroundColor : 'white',
        marginRight: 10,
    },
    imageItem : {
        flex :1, 
        width: null,
        height: null
    }, 
    categoryTitle : {
        fontSize: 22, 
        color: '#6666FF', 
        textDecorationLine : 'underline'
    },
    scrollView: {
        marginTop : 5,
    }
});

export default style