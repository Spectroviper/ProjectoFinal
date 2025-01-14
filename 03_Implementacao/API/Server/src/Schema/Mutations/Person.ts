import { GraphQLID, GraphQLInt, GraphQLString } from "graphql";
import { MessageType } from "../TypeDef/Messages";
import { Persons } from "../../Entities/Persons";
import { Games } from "../../Entities/Games";
import { PersonAchievements } from "../../Entities/PersonAchievements";


export const CREATE_PERSON = {
    type: MessageType,
    args: {
        UserName: { type: GraphQLString },
        Email: { type: GraphQLString },
        Biography: { type: GraphQLString },
        TotalPoints: { type: GraphQLInt },
        AverageCompletion: {  type: GraphQLInt },
        Image: { type: GraphQLString }
    },
    async resolve(parent: any, args: any) {
        const { UserName,
            Email,
            Biography,
            TotalPoints,
            AverageCompletion,
            Image} = args;
        
        const currentDate = new Date();

        const lowestRank = await Persons
            .createQueryBuilder("persons")
            .select("MAX(persons.SiteRank)", "max")
            .getRawOne();
        
        const newSiteRank = lowestRank.max ? lowestRank.max + 1 : 1;

        
        const newPerson = new Persons();
        newPerson.UserName = UserName;
        newPerson.Email = Email;
        newPerson.Biography = Biography;
        newPerson.MemberSince = currentDate;
        newPerson.LastLogin = currentDate;
        newPerson.TotalPoints = TotalPoints;
        newPerson.AverageCompletion = AverageCompletion;
        newPerson.Image = Image;
        newPerson.SiteRank = newSiteRank;
        
        if (UserName === newPerson.UserName) {
            await Persons.insert(newPerson);
            return {successful: true, message: "PERSON CREATED SUCCESSFULLY"}
        } else{
            throw new Error("PERSON DOES NOT HAVE THE RIGHT USERNAME!");
        }

    }
};

export const UPDATE_PERSON = {
    type: MessageType,
    args: {
        UserName: { type: GraphQLString },
        NewUserName: { type: GraphQLString},
        NewBiography: { type: GraphQLString },
        NewImage: { type: GraphQLString}
        
    },
    async resolve(parent: any, args: any) {
        const {UserName, NewUserName, NewBiography, NewImage} = args
        const person = await Persons.findOne({where: {UserName: UserName}})
        const personUserName = person?.UserName;

        if (UserName === personUserName) {
            await Persons.update({UserName: UserName},{UserName: NewUserName, Biography: NewBiography, Image: NewImage});
            return {successful: true, message: "PERSON UPDATED SUCCESSFULLY"}
        } else{
            throw new Error("PERSON DOES NOT HAVE THE RIGHT USERNAME!");
        }
    }
};

export const SIGN_OUT = {
    type: MessageType,
    args: {
        Email: { type: GraphQLString }
    },
    async resolve(parent: any, args: any) {
        const {Email} = args;
        const person = await Persons.findOne({where: {Email: Email}, relations: ['Plays', 'PersonAchievements', 'PersonAchievements.Achievement']});
        const personEmail = person?.Email;

        if(person){
            if(personEmail === Email) {
                await Persons.update({Email: Email},{LastLogin: new Date()});
                return {successful: true, message: "PERSON UPDATED SUCCESSFULLY"}
            }
            else{
                throw new Error("PERSON DOES NOT HAVE THE RIGHT USER_NAME!");
            }
        }
        else{
            throw new Error("PERSON NOT FOUND!");
        }
        
    }
};

export const ADD_GAME = {
    type: MessageType,
    args: {
        UserName: { type: GraphQLString },
        GameName: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {UserName, GameName} = args
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['Plays', 'PersonAchievements']})
        const game = await Games.findOne({where: {GameName: GameName}, relations: ['Achievements']})
        const achievements = game?.Achievements || [];
        const personUserName = person?.UserName;
        const gameGameName = game?.GameName;
        

        if (UserName === personUserName && GameName === gameGameName) {
            if(person && game) {
                let personPlays = person?.Plays || [];
                let personAchievements = person?.PersonAchievements || [];
                if(!personPlays.includes(game)) {
                    personPlays.push(game);
                    person.Plays = personPlays;
                    if(achievements) {
                        achievements.forEach(achievement => {
                            const personAchievement = new PersonAchievements();
                            personAchievement.Clear = false;
                            personAchievement.Achievement = achievement;
                            personAchievement.Person = person;
                            PersonAchievements.insert(personAchievement);
                            personAchievements.push(personAchievement);
                            person.PersonAchievements = personAchievements;
                        });
                    }
                    await person.save();
                    return {successful: true, message: "GAME ADDED SUCCESSFULLY"}
                }
                else{
                    throw new Error("GAME IS ALREADY INCLUDED IN PERSONS LYBRARY");
                } 
            }
            else{
                throw new Error("THERE IS NO PERSON OR GAME WITH SUCH A NAME!");
            }  
        } else{
            throw new Error("PERSON OR GAME DO NOT HAVE THE RIGHT USERNAME!");
        }
    }
};

export const REMOVE_GAME = {
    type: MessageType,
    args: {
        UserName: { type: GraphQLString },
        GameName: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {UserName, GameName} = args
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['Plays', 'PersonAchievements', 'PersonAchievements.Achievement']})
        const game = await Games.findOne({where: {GameName: GameName}, relations: ['Achievements']})
        const personUserName = person?.UserName;

        if (UserName === personUserName) {
            if(person && game) {
                let personPlays = person?.Plays || [];
                let personAchievements = person?.PersonAchievements || [];

                const gameIncludedInPersonPlays = personPlays.some(play => play.id === game.id);
                
                if(gameIncludedInPersonPlays) {
                    const achievementsIds = game.Achievements.map(achievement => achievement.id);

                    
                    const personAchievementsToDelete = personAchievements.filter(personAchievement =>
                        achievementsIds.includes(personAchievement.Achievement.id)
                    );

                    const updatedPersonAchievements = personAchievements.filter(personAchievement =>
                        !achievementsIds.includes(personAchievement.Achievement.id)
                    );

                    person.PersonAchievements = updatedPersonAchievements;
                    person.Plays = personPlays.filter(play => play.id !== game.id);
                    
                    await Promise.all(personAchievementsToDelete.map(async (personAchievement) => {
                        await personAchievement.remove();
                    }));

                    await person.save();

                    return {successful: true, message: "GAME REMOVED SUCCESSFULLY"}
                }
                else{
                    throw new Error("GAME IS NOT INCLUDED IN PERSONS LYBRARY");
                } 
            }
            else{
                throw new Error("THERE IS NO PERSON OR GAME WITH SUCH A NAME!");
            }  
        } else{
            throw new Error("PERSON OR GAME DO NOT HAVE THE RIGHT USERNAME!");
        }
    }
};


export const TOGGLE_UNLOCK_ACHIEVEMENT = {
    type: MessageType,
    args: {
        UserName: { type: GraphQLString },
        personAchievementId: { type: GraphQLID }
    },
    async resolve(parent: any, args: any) {
        const { UserName, personAchievementId } = args;
        const person = await Persons.findOne({where: {UserName: UserName}, relations: ['PersonAchievements']});
        const personPersonName = person?.UserName;
        

        if(person){
            if (UserName == personPersonName) {
                const personAchievementToUnlock = await PersonAchievements.findOne({where: {id: personAchievementId}})
 
                if(personAchievementToUnlock) {
                    const personAchievementsIds = person.PersonAchievements.map(personAchievement => personAchievement.id);

                    if(personAchievementsIds.includes(personAchievementToUnlock.id)) {
                        let personTotalPoints = person?.TotalPoints;
                        const personAchievementToUnlockRetroPoints = personAchievementToUnlock.Achievement.RetroPoints;
                        if(personAchievementToUnlock.Clear == false) {
                            personTotalPoints += personAchievementToUnlockRetroPoints;
                            await Persons.update({UserName: UserName},{TotalPoints: personTotalPoints});

                            const persons = await Persons.find({ order: { TotalPoints: "DESC" } });
                            let rank = 1;
                            for (const person of persons) {
                                person.SiteRank = rank;
                                await person.save();
                                rank++;
                            }

                            personAchievementToUnlock.Clear = true;
                            personAchievementToUnlock.UnlockDate = new Date();
                            await personAchievementToUnlock.save();

                            return {successful: true, message: "ACHIEVEMENT UNLOCKED!"}
                        }
                        if(personAchievementToUnlock.Clear == true){
                            personTotalPoints -= personAchievementToUnlockRetroPoints;
                            await Persons.update({UserName: UserName},{TotalPoints: personTotalPoints});

                            const persons = await Persons.find({ order: { TotalPoints: "DESC" } });
                            let rank = 1;
                            for (const person of persons) {
                                person.SiteRank = rank;
                                await person.save();
                                rank++;
                            }

                            personAchievementToUnlock.Clear = false;
                            //personAchievementToUnlock.UnlockDate = null;
                            await personAchievementToUnlock.save();


                            return {successful: true, message: "ACHIEVEMENT LOCKED!"}
                        }
                    }
                    else{
                        throw new Error("PERSON ACHIEVEMENT NOT INCLUDED IN THE REQUESTED PERSONS ACHIEVEMENTS LIST!");
                    }
                }
                else{
                    throw new Error("PERSON ACHIEVEMENT NOT FOUND");
                }
            }
            else{
                throw new Error("PERSON DOES NOT HAVE THE RIGHT ACHIEVEMENT_NAME!");
            }
        }
        else{
            throw new Error("COULD NOT FIND PERSON");
        }
        
        
    }
};

export const DELETE_PERSON = {
    type: MessageType,
    args: {
        id: { type: GraphQLID},
    },
    async resolve(parent: any, args: any) {
        const id = args.id;
        await Persons.delete(id)
        return {successful: true, message: "PERSON DELEATED SUCCESFULLY"}
    }
};