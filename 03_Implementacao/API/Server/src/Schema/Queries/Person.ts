import { GraphQLInt, GraphQLList, GraphQLString } from 'graphql'
import {PersonType} from '../TypeDef/Person'
import { Persons } from '../../Entities/Persons';
import { GameType } from '../TypeDef/Game';
import { PersonAchievementType } from '../TypeDef/PersonAchievementType';
import { PersonAchievements } from '../../Entities/PersonAchievements';

export const GET_ALL_PERSONS = {
    type: new GraphQLList(PersonType),
    resolve() {
        return Persons.find();
    }
}

export const GET_PERSON = {
    type: PersonType,
    args: {
        UserName: { type: GraphQLString }
    },
    async resolve(parent: any, args: any) {
        const {UserName} = args;
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['Plays', 'PersonAchievements', 'PersonAchievements.Achievement']});
        const personUserName = person?.UserName;

        if(personUserName === UserName) {
            return person;
        }
        else{
            throw new Error("PERSON DOES NOT HAVE THE RIGHT USER_NAME!");
        }
    }
}

export const SIGN_IN = {
    type: PersonType,
    args: {
        Email: { type: GraphQLString }
    },
    async resolve(parent: any, args: any) {
        const {Email} = args;
        const person = await Persons.findOne({where: {Email: Email}, relations: ['Plays', 'PersonAchievements', 'PersonAchievements.Achievement']});
        const personEmail = person?.Email;

        if(person){
            if(personEmail === Email) {
                person.LastLogin = new Date()
                return person;
            }
            else{
                throw new Error("PERSON DOES NOT HAVE THE RIGHT USER_NAME!");
            }
        }
        else{
            throw new Error("PERSON NOT FOUND!");
        }
        
    }
}

export const GET_PERSONS_GAMES = {
    type: new GraphQLList(GameType),
    args: {
        UserName: { type: GraphQLString },
    },
    async resolve(parent: any, args: any) {
        const {UserName} = args
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['Plays']})
        const personUserName = person?.UserName;

        if (UserName === personUserName) {
            const personPlays = person?.Plays || [];
            if(personPlays) {
                return personPlays
            }
            else{
                throw new Error("PERSON DOES NOT HAVE ANY GAMES");
            }
            
        }
        else{
            throw new Error("PERSON DOES NOT HAVE THE RIGHT USERNAME!");
        }
    }
}

export const GET_PERSONS_ACHIEVEMENTS = {
    type: new GraphQLList(PersonAchievementType),
    args: {
        UserName: { type: GraphQLString },
    },
    async resolve(parent: any, args: any) {
        const {UserName} = args
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['PersonAchievements', 'PersonAchievements.Achievement']})
        const personUserName = person?.UserName;

        if (UserName === personUserName) {
            const personAchievements = person?.PersonAchievements || [];
            if(personAchievements) {
                return personAchievements
            }
            else{
                throw new Error("PERSON DOES NOT HAVE ANY ACHIEVEMENTS");
            }
            
        }
        else{
            throw new Error("PERSON DOES NOT HAVE THE RIGHT USERNAME!");
        }
    }
}