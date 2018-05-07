import React , {Component} from 'react'
import {Container, Header, Content, Footer, FooterTab, Right, Body, Left, Button, Icon, Input, Item, Text, Root, ListItem, CheckBo,Title, Badge } from 'native-base'
import {View, TextInput, Dimensions, TouchableOpacity} from 'react-native'
import { Font, AppLoading } from 'expo';
import style  from './style'
// import MapView from '../Maps/index'
import ListDemo from '../ListView/index'

class SearchPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loading: true,
            isSearch: false,
            searchStr: '',
            hasResultSearch : false,
        }
        this._onSearchIconPress = this._onSearchIconPress.bind(this);
        this._backToLogin = this._backToLogin.bind(this);
        this._cleanSearchString = this._cleanSearchString.bind(this);
      }


      static navigationOptions = ({navigation}) => {
        return {
            headerTitle: '',
            header : null,
        }
    }

      _onSearchIconPress = (status) => {
        this.setState({ isSearch : status});
      };
      _backToLogin = () => {
        this.props.navigation.goBack();
      }
      _cleanSearchString = () => {
          this.setState({searchStr: ''})
      }


    async componentWillMount() {
        await Font.loadAsync({
          Roboto: require("native-base/Fonts/Roboto.ttf"),
          Roboto_medium: require("native-base/Fonts/Roboto_medium.ttf")
        });
        this.setState({ 
            loading: false ,
        });
      }


    render() {
        const { handleSubmit } = this.props;
        if (this.state.loading) {
            return (
              <Root>
                <AppLoading />
              </Root>
            );
          }
        return (
            <Container>
                <Header searchBar style = {style.header}>
                    <Item>
                        <Icon  name="search" />
                        <Input 
                            placeholder="Search..." 
                            returnKeyType = {"search"}
                            onChange = {(searchStr) => {this.setState({ searchStr})}}
                            value = {this.state.searchStr}
                        />
                        {this.state.searchStr == ''
                        ? <Icon  name="close" onPress = {this._backToLogin}/>
                        : <Icon  name="close" onPress = {this._cleanSearchString}/> }
                    </Item>
                    <Button transparent>
                        <Text>Search</Text>
                    </Button>
                    {/* <Right>
                        <Button 
                            transparent 
                            onPress = {(status) => {
                                this._onSearchIconPress(false);
                            }}>
                            <Icon name='close' />
                        </Button>
                        
                    </Right> */}
                </Header>
  
                <Content>
                    {/* <MapView />  */}
                    <ListDemo />
                </Content>
            </Container>
        );
    }
}

export default SearchPage;