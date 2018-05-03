import React , { Component } from 'react'
import { View, TextInput, Dimensions, Image, ScrollView, FlatList } from 'react-native'
import { Container, Content, Icon, Button, Text, Root, Header, Right, Left, Body, Thumbnail, Title } from 'native-base'
import style from './style'
import { Font, AppLoading } from "expo";

const data = [
    {
        itemName: 'No1',
    },
    {
        itemName: 'No2',
    },
    {
        itemName: 'No3',
    },
    {
        itemName: 'No4',
    },
    {
        itemName: 'No5',
    },
    {
        itemName: 'No6',
    },

]


class Homepage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
        }
        this._stepToSearchPage = this._stepToSearchPage.bind(this);
      }

      async componentWillMount() {
        await Font.loadAsync({
          Roboto: require("native-base/Fonts/Roboto.ttf"),
          Roboto_medium: require("native-base/Fonts/Roboto_medium.ttf")
        });
        this.setState({ loading: false });
      }

      _stepToSearchPage = () => {
        this.props.navigation.navigate('searchpage');
      }

      static navigationOptions = () => {
        return {
            header : null,
        }
      }

      _renderCategoryItem = ({item}) => {
        return (
            <View style={{height :90}}>
                <View
                    style = {style.itemInScroolView}
                >
                    <Image resizeMode="contain" source={require('../../../resources/images/grab_logo.png')} style = {style.imageItem}/>
                    
                </View>
                <View>
                    <Text>
                    {item.itemName}
                    </Text>
                </View>
            </View>
            
        )
    }

    render () {
        if (this.state.loading) {
            return (
              <Root>
                <AppLoading />
              </Root>
            );
          }
        return (
            <Container>
                <Header>
                    <Left>
                        <Button transparent>
                            <Icon name='ios-expand' />
                        </Button>
                    </Left>
                    <Body>
                        <Title>Homepage</Title>
                    </Body>
                    <Right>
                        <Button transparent>
                            <Icon name='search' onPress = {this._stepToSearchPage}/>
                        </Button>
                        <Button transparent>
                            <Thumbnail small source={require('../../../resources/images/login_background.jpg')} />
                        </Button>
                    </Right>
                </Header>
                <Content>
                    <View style= {style.category}>
                        <View style={style.mainCategory}>
                            <Text style = {style.categoryTitle}>Danh mục</Text>
                            <ScrollView
                                horizontal={true}
                                style = {style.scrollView}
                            >
                                <FlatList
                                    renderItem={this._renderCategoryItem}
                                    data = {data}
                                    horizontal
                                ></FlatList>
                            </ScrollView>
                        </View>
                    </View>
                </Content>
            </Container>
        );
    }
} 

export default Homepage ;