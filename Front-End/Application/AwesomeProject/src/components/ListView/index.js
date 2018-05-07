import React, { Component } from 'react'
import { View, TextInput, Dimensions, FlatList, TouchableOpacity, Image, ScrollView } from 'react-native'
import { Container, Content, Icon, Button, Text, List, ListItem, Root, Right } from 'native-base'
import { connect } from 'react-redux'
import { Font, AppLoading } from 'expo'

const storeData = [
    {
        nameStore  : 'Thinh Cuong',
        distance : 2,
    },{
        nameStore  : 'Thinh Cuong 2',
        distance : 4,
    },{
        nameStore  : 'Thinh Cuong 3',
        distance : 6,
    },{
        nameStore  : 'Thinh Cuong 4',
        distance : 6,
    },{
        nameStore  : 'Thinh Cuong 5',
        distance : 6,
    },{
        nameStore  : 'Thinh Cuong 6',
        distance : 6,
    },{
        nameStore  : 'Thinh Cuong 7',
        distance : 6,
    },
    {
        nameStore  : 'Thinh Cuong 7',
        distance : 6,
    },
    {
        nameStore  : 'Thinh Cuong 7',
        distance : 6,
    },
    {
        nameStore  : 'Thinh Cuong 7',
        distance : 6,
    },
    {
        nameStore  : 'Thinh Cuong 7',
        distance : 6,
    },

]

class ListDemo extends Component {

    // static navigationOptions = ({navigation}) => {
    //     return {
    //         headerTitle: navigation.state.params.itemName
    //     }
    // }

    // _onLogin = () => {
    //     this.props.navigation.goBack()
    // }

    constructor(props) {
        super(props);
        this.state = {
            loading: true,isSearch: false,
        }
      }

      async componentWillMount() {
        await Font.loadAsync({
          Roboto: require("native-base/Fonts/Roboto.ttf"),
          Roboto_medium: require("native-base/Fonts/Roboto_medium.ttf")
        });
        this.setState({ 
            loading: false ,
            isSearch : false
        });
      }

      //create display component for list view
    _renderItem = ({item}) => {
        return (
            <View style = {{
                flexDirection : 'row',
                justifyContent : 'flex-start',
                alignContent : 'flex-start',
                width : Dimensions.get('screen').width,
                marginBottom: 10,
                flex: 1,
            }}>
            <Image 
                source={require('../../../resources/images/grab_logo.png')} 
                style={{
                    width: 100,
                    height: 70,
                    marginRight: 10,
                    flex: 1,
                }}    
            />
            <View 
                style ={{
                    flexDirection : 'column',
                    justifyContent : 'center',
                    alignContent : 'center',
                    flex: 3
                }}
            >
                <Text style = {{fontWeight : 'bold',}}>{item.nameStore}</Text>
                <Text>Distance : {item.distance}</Text>
            </View>
            <View 
                style ={{
                    flexDirection : 'column',
                    justifyContent : 'center',
                    alignContent : 'center',
                    flex: 1
                }}
            >
                    <Button transparent>
                        <Text>Store</Text>
                    </Button>
            </View>
            </View>
        )
    }
    render() {
        if (this.state.loading) {
            return (
              <Root>
                <AppLoading />
              </Root>
            );
          }
        return (
            <Container>
                <View>
                    <FlatList
                        renderItem={this._renderItem}
                        data={storeData}
                        keyExtractor={(storeData, index) => index + ''}
                    />
                    </View>
            </Container>
        )
    }
}


export default ListDemo;