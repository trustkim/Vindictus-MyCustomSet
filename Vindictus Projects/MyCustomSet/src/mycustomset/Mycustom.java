package mycustomset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * ������ ������ 80�� Ŀ���� ��Ʈ�� ¥�� ���α׷�
 * 80�� �� ���� �� Ư�� ������ ���� ���� ������ ã�´�.
 * �ش� �������� 3�� ���� ����� ������ 90�� �����ۺ��� ���� �� ���� �� ����.
 */
public class Mycustom {
	public static void main(String args[]){
		int set_count=0;
		Equipment[] equips=null;
		System.out.println("������ ������ Ŀ���� ��Ʈ ���� ���α׷��Դϴ�.");
		// �ؽ�Ʈ ������ �о� �����ϰ��� �ϴ� �ҽ� ������ �Է� �޴´�. �Է� �ް� ȭ�� ���.
		// �� ������ ��� ���� ������ ��츦 �����Ͽ� ���� ���� ������������ �����Ѵ�. ���.
		try {
			Scanner input = new Scanner(new File("input.txt"));
			System.out.print("������ ����� �н��ϴ�.\n"+"��� �̸�: <"+input.nextLine()+"> ");
			set_count = input.nextInt(); input.nextLine();
			System.out.println("���� ��Ʈ �������� "+set_count+"�� �Դϴ�.");
			
			equips = new Equipment[5*set_count];
			for(int i=0;i<5*set_count;i++) equips[i] = new Equipment();
			for(int i=0;i<5*set_count;i+=5){
				String cur_setname = input.nextLine(); System.out.println(cur_setname+"�д� ��...");
				for(int j=0;j<5;j++){
					equips[i+j].setSetname(cur_setname);	// ��Ʈ �̸� ����
				}
				//for(int j=0;j<5;j++) equips[i+j].Print();
				for(int j=0;j<5;j++){
					String temp = input.nextLine();
					String[] result = temp.split("\\|");
					String temp_name = result[0].trim();			// ��� �̸�
					result = temp.split("\\|")[1].split(",");
					String temp_type1 = result[0].trim();			// ��� ����
					String temp_type2 = result[1].trim();			// ���� ����
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
			System.out.println("���� �б� �Ϸ�");
			
			input.close();
		} catch (FileNotFoundException e) { System.out.println("x"); }
		for(int j=0;j<5*set_count;j++) equips[j].Print();	// ��ü ��� ���
		System.out.println("Ŀ���� ���� ����");
		ArrayList<Equipment>[] customs = new ArrayList[5];
		for(int i=0;i<5;i++) customs[i]=new ArrayList();
		for(int i=0;i<5*set_count;i++){
			if(equips[i].type2.trim().equals("�Ӹ� ��")) customs[0].add(equips[i]);
			else if(equips[i].type2.trim().equals("���� ��")) customs[1].add(equips[i]);
			else if(equips[i].type2.trim().equals("�ٸ� ��")) customs[2].add(equips[i]);
			else if(equips[i].type2.trim().equals("�� ��")) customs[3].add(equips[i]);
			else if(equips[i].type2.trim().equals("�� ��")) customs[4].add(equips[i]);
		}
		//helms.get(1).Print();
		//System.out.println(helms.size());
		try{
			PrintWriter out = new PrintWriter(new FileWriter("output.csv"));
			out.println("�Ӹ�,����,�ٸ�,��,��,����,��,��ø,����,����,ũ��Ƽ�� ����,���¹̳�,��Ʈ���ʽ� ����,��Ʈ���ʽ� ��");
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
			 } // ��ü ���� ����Ʈ ���
			out.close();
		}catch(IOException e) { e.printStackTrace();}

		
		System.out.println("���� ����");
	}
	static Status parseStatus(String input){
		Status result = new Status();
		String[] temp = input.split(" ");
		for(int i=0;i<temp.length;i+=2){
			if(temp[i].equals("����")){
				result.def = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("ũ��Ƽ������")){
				result.crt_r = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("��")){
				result.str = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("��ø")){
				result.agl = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("����")){
				result.intl = Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("����")){
				result.will= Integer.parseInt(temp[i+1]);
			}else if(temp[i].equals("���¹̳�")){
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
	String name;	// ������ �̸�
	int type=0;		// ������ ���� (��: ����Ʈ ������, ��, ����...)
}

class Equipment extends Item {
	int level=80; 		// ���� ����
	int quality=2;	// ǰ��
	String type1;		// �� ����(0:����, 1:õ ��, 2:�氩, 3:�߰�, 4:�÷���Ʈ)
	String type2;		// ���� ����(1:�Ӹ�, 2:����, 3:�ٸ�, 4:��, 5:��)
	Status stat;	// ����
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
		System.out.println("����: "+stat.def);
		System.out.println("��: "+stat.str);
		System.out.println("��ø: "+stat.agl);
		System.out.println("����: "+stat.intl);
		System.out.println("����: "+stat.will);
		System.out.println("ũ��Ƽ�� ����: "+stat.crt_r);
		System.out.println("���¹̳�: "+stat.stm);
		System.out.println("��Ʈ����: "+setbonus.setname);
		System.out.println("2�� ���� �����: ���� +"+setbonus.bonus[0].def+", �� +"+setbonus.bonus[0].str);
		System.out.println("3�� ���� �����: ���� +"+setbonus.bonus[1].def+", �� +"+setbonus.bonus[1].str);
		System.out.println("4�� ���� �����: ���� +"+setbonus.bonus[2].def+", �� +"+setbonus.bonus[2].str);
		System.out.println("5�� ���� �����: ���� +"+setbonus.bonus[3].def+", �� +"+setbonus.bonus[3].str);
		System.out.println("-------------------------------------------------------");
	}
}

class Status {
	int def;		// ����
	int str;		// ��
	int agl;		// ��ø
	int will;		// ����
	int intl;		// ����
	int crt_r;		// ũ��Ƽ�� ����
	int stm;		// ���׹̳�
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