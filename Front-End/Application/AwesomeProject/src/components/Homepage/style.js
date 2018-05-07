import {StyleSheet, Dimensions} from 'react-native'

const style = StyleSheet.create({
    category: {
        marginTop : 10,
        backgroundColor : '#DDDDDD',
        height: 130,
    },
    mainCategory : {
        marginLeft: 10,
        marginBottom: 5,
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
        fontSize: 23, 
        color: 'black', 
        flex: 7
    },
    scrollView: {
        marginTop : 5,
    },
    categoryControlBar : {
        flexDirection: "row",
        alignItems: 'flex-start',
        justifyContent : 'flex-start',
    },
    seeMoreButton: {
        flex: 2,
        marginTop : 2,
    }
});

export default style