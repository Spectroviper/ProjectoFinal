import { BaseEntity, Column, Entity, JoinTable, ManyToMany, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Achievements } from "./Achievements";
import { Persons } from "./Persons";

@Entity()
export class Games extends BaseEntity {

    @PrimaryGeneratedColumn()
    id!: number;

    @Column()
    GameName!: string;

    @Column({nullable: true})
    About!: string;

    @Column()
    Console!: string;
    
    @Column()
    Developer!: string;

    @Column()
    Publisher!: string;

    @Column()
    Genre!: string;

    @Column()
    ReleaseDate!: Date;

    @Column({nullable: true})
    Image!: Buffer;

    Players!: Persons[];

    @OneToMany(() => Achievements, achievements => achievements.Game)
    Achievements!: Achievements[];

}