package mycustomset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * 마비노기 영웅전 80제 커스텀 세트를 짜는 프로그램
 * 80제 방어구 조합 중 특정 스탯이 가장 높은 조합을 찾는다.
 * 해당 조합으로 3성 방어구를 만들어 입으면 90제 보라템보다 좋을 수 있을 것 같다.
 */
public class Mycustom {
	public static void main(String args[]){
		int set_count=0;
		Equipment[] equips=null;
		System.out.println("마비노기 영웅전 커스텀 세트 산출 프로그램입니다.");
		// 텍스트 파일을 읽어 산출하고자 하는 소스 장비들을 입력 받는다. 입력 받고 화면 출력.
		// 각 부위별 모든 조합 가능한 경우를 산출하여 힘에 대한 오름차순으로 정렬한다. 출력.
		try {
			Scanner input = new Scanner(new File("input.txt"));
			System.out.print("아이템 목록을 읽습니다.\n"+"목록 이름: <"+input.nextLine()+"> ");
			set_count = input.nextInt(); input.nextLine();
			System.out.println("읽을 세트 아이템은 "+set_count+"개 입니다.");
			
			equips = new Equipment[5*set_count];
			for(int i=0;i<5*set_count;i++) equips[i] = new Equipment();
			for(int i=0;i<5*set_count;i+=5){
				String cur_setname = input.nextLine(); System.out.println(cur_setname+"읽는 중...");
				for(int j=0;j<5;j++){
					equips[i+j].setSetname(cur_setname);	// 세트 이름 읽음
				}
				//for(int j=0;j<5;j++) equips[i+j].Print();
				for(int j=0;j<5;j++){
					String temp = input.nextLine();
					String[] result = temp.split("\\|");
					String temp_name = result[0].trim();			// 장비 이름
					result = temp.split("\\|")[1].split(",");
					String temp_type1 = result[0].trim();			// 장비 종류
					String temp_type2 = result[1].trim();			// 착용 부위
					equips[i+j].setEquip(temp_name, temp_type1, temp_type2, parseStatus(input.nextLine()));
				}
				for(int j=0;j<4;j++){
					String temp = input.nextLine();
					String[] result = temp.split(":");
					for(int k=0;k<5;k++)
						equips[i+k].setbonus.bonus[j] = parseStatus(result[1].substring(1));
					//System.out.println(result[1]);
				}
			}
			System.out.println("파일 읽기 완료");
			
			input.close();
		} catch (FileNotFoundException e) { System.out.println("x"); }
		for(int j=0;j<5*set_count;j++) equips[j].Print();	// 전체 목록 출력
		System.out.println("커스텀 조합 시작");
		ArrayList<Equipment>[] customs = new ArrayList[5];
		for(int i=0;i<5;i++) customs[i]=new ArrayList();
		for(int i=0;i<5*set_count;i++){
			if(equips[i].type2.trim().equals("머리 방어구")) customs[0].add(equips[i]);
			else if(equips[i].type2.trim().equals("가슴 방어구")) customs[1].add(equips[i]);
			else if(equips[i].type2.trim().equals("다리 방어구")) customs[2].add(equips[i]);
			else if(equips[i].type2.trim().equals("손 방어구")) customs[3].add(equips[i]);
			else if(equips[i].type2.trim().equals("발 방어구")) customs[4].add(equips[i]);
		}
		//helms.get(1).Print();
		//System.out.println(helms.size());
		try{
			PrintWriter out = new PrintWriter(new FileWriter("output.csv"));
			out.println("머리,가슴,다리,손,발,방어력,힘,민첩,지능,의지,크리티컬 저항,스태미나,세트보너스 방어력,세트보너스 힘");
			for(int head=0;head<customs[0].size();head++){
				for(int chest=0;chest<customs[1].size();chest++){
					for(int leg=0;leg<customs[2].size();leg++){
						for(int hand=0;hand<customs[3].size();hand++){
							for(int foot=0;foot<customs[4].size();foot++){
								out.print(customs[0].get(head).name+",");
								out.print(customs[1].get(chest).name+",");
								out.printf("%s,",customs[2].get(leg).name);
								//out.print(greaves.get(leg).name+",");
								out.print(customs[3].get(hand).name+",");
								//out.print(customs[4].get(foot).name+",");
								out.printf("%s,",customs[4].get(foot).name);
								int i=0; Status BonusStat = getBonusStat(customs[i++].get(head),customs[i++].get(chest),customs[i++].get(leg),customs[i++].get(hand),customs[i++].get(foot));//System.out.print(BonusStat);
								i=0;Status sum_stat = customs[i++].get(head).stat.add(customs[i++].get(chest).stat).add(customs[i++].get(leg).stat).add(customs[i++].get(hand).stat).add(customs[i++].get(foot).stat).add(BonusStat);
								out.print(sum_stat); out.print(","+BonusStat.def+","+BonusStat.str);
								out.println();
			 				}// print(sum_stat);
			 			}
			 		}
			 	}
			 } // 전체 조합 리스트 출력
			out.close();
		}catch(IOException e) { e.printStackTrace();}

		
		System.out.println("연산 종료");
	}
	static Status parseStatus(String input){
		Status result = new Status();
		String[] temp = input.split(" ");
		for(int i=0;i<temp.length;i+=2){
			if(temp[i].equals("방어력")){
				result.def = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("크리티컬저항")){
				result.crt_r = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("힘")){
				result.str = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("민첩")){
				result.agl = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("지능")){
				result.intl = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("의지")){
				result.will= Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("스태미나")){
				result.stm = Integer.parseInt(temp[i+1]);
			}
		}
		return result;
	}
	static Status getBonusStat(Equipment head, Equipment chest, Equipment leg, Equipment hand, Equipment foot){
		Status result = new Status();
		Equipment[] equips = {head, chest, leg, hand, foot};
		class SetTable{
			String name;
			int count;
			SetTable(String name){this.name=name;count=1;}
		}
		ArrayList<SetTable> table = new ArrayList<SetTable>();
		//SetTable[] table = new SetTable[5];
		table.add(new SetTable(head.setbonus.setname));//System.out.println(table.get(0).name+", "+table.get(0).count);
		for(int i=1;i<5;i++){
			for(int j=0;j<table.size();j++){
				if(equips[i].setbonus.setname.equals(table.get(j).name)){
					table.get(j).count++; break;
				}
				if(j==table.size()-1){
					table.add(new SetTable(equips[i].setbonus.setname)); break;
				}
			}
		}
		for(int i=0;i<table.size();i++){
			if(table.get(i).count>1){
				for(int j=0;j<5;j++){
					//System.out.println(result.add(equips[j].setbonus.bonus[table.get(i).count-2]));
					if(equips[j].setbonus.setname.equals(table.get(i).name)){
						result = result.add(equips[j].setbonus.bonus[table.get(i).count-2]); break;
					}
				}
			}
		}
		//System.out.println(result);
		return result;
	}
}

class Item {
	String name;	// 아이템 이름
	int type=0;		// 아이템 종류 (예: 퀘스트 아이템, 방어구, 무기...)
}

class Equipment extends Item {
	int level=80; 		// 착용 레벨
	int quality=2;	// 품질
	String type1;		// 방어구 종류(0:무기, 1:천 방어구, 2:경갑, 3:중갑, 4:플레이트)
	String type2;		// 착용 부위(1:머리, 2:가슴, 3:다리, 4:손, 5:발)
	Status stat;	// 스탯
	SetEquip_atrb setbonus;
	Equipment(){
		stat = new Status(); setbonus = new SetEquip_atrb();
	}
	void setSetname(String name){
		setbonus.setname = name;
	}
	void setEquip(String name, String type1, String type2, Status stat){
		this.name=name;this.type1=type1;this.type2=type2;this.stat=stat;
	}
	public void Print() {
		System.out.println(name+"\t"+type1+"\t"+type2+"\t");
		System.out.println("방어력: "+stat.def);
		System.out.println("힘: "+stat.str);
		System.out.println("민첩: "+stat.agl);
		System.out.println("지능: "+stat.intl);
		System.out.println("의지: "+stat.will);
		System.out.println("크리티컬 저항: "+stat.crt_r);
		System.out.println("스태미나: "+stat.stm);
		System.out.println("세트정보: "+setbonus.setname);
		System.out.println("2개 부위 착용시: 방어력 +"+setbonus.bonus[0].def+", 힘 +"+setbonus.bonus[0].str);
		System.out.println("3개 부위 착용시: 방어력 +"+setbonus.bonus[1].def+", 힘 +"+setbonus.bonus[1].str);
		System.out.println("4개 부위 착용시: 방어력 +"+setbonus.bonus[2].def+", 힘 +"+setbonus.bonus[2].str);
		System.out.println("5개 부위 착용시: 방어력 +"+setbonus.bonus[3].def+", 힘 +"+setbonus.bonus[3].str);
		System.out.println("-------------------------------------------------------");
	}
}

class Status {
	int def;		// 방어력
	int str;		// 힘
	int agl;		// 민첩
	int will;		// 의지
	int intl;		// 지능
	int crt_r;		// 크리티컬 저항
	int stm;		// 스테미나
	Status(){def = str = agl = will = intl = crt_r = stm = 0;}
	Status(int def, int crt_r, int str, int agl, int intl, int will, int stm){
		this.def=def;this.str=str;this.agl=agl;this.will=will;this.intl=intl;this.crt_r=crt_r;this.stm=stm;
	}
	public String toString(){
		return (""+def+","+str+","+agl+","+intl+","+will+","+crt_r+","+stm);
	}
	public Status add(Status B){
		Status result = new Status(def+B.def,crt_r+B.crt_r,str+B.str,agl+B.agl,intl+B.intl,will+B.will,stm+B.stm);
		return result;
	}
}

class SetEquip_atrb {
	String setname;
	Status[] bonus;
	SetEquip_atrb(){
		bonus = new Status[4]; for(int i=0;i<4;i++) bonus[i] = new Status();
	}
}