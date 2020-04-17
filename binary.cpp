#include<iostream>
#include<bitset>
#include<cmath>
short relayMaptoInt(uint8_t input){
    uint8_t out=1;
    if(input==0b00000000){
        return 0;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & 0b10000000)!=0b10000000){
            if(out==input){
                return count;
            }
            count++;
            out=out<<1;
        }
        if(out==input){
            return count;
        }
    }
    return -1;
}
uint8_t inttoRelayMap(short input){
    uint8_t out=1;
    if(input==0){
        return 0b00000000;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & 0b10000000)!=0b10000000){
            if(count==input){
                return out;
            }
            count++;
            out=out<<1;
        }
        if(count==input){
            return out;
        }
    }
    return 0b11111111;
}
struct relayConf{
  uint8_t r1,r2,r3,r4;  
} relayconf;
//r1=0b00000011 r2=0b00001100 r3=0b00110000 r4=0b11000000
//        1                  2           3          4
//0000  00001              00010        00011       00100
void relayConfLoadEEPROM(uint8_t *);
void relayConfDumpEEPROM(){
    uint8_t out[3];
    short r1=relayMaptoInt(relayconf.r1),r2=relayMaptoInt(relayconf.r2),r3=relayMaptoInt(relayconf.r3),r4=relayMaptoInt(relayconf.r4);
    // std::cout<<r1<<" "<<unsigned(relayconf.r2)<<std::endl;
    uint32_t temp=r1 | r2<<5 | r3 << 10 | r4 <<15;
    // std::cout<<std::bitset<32>(temp)<<std::endl;
    out[0]= 255 & temp;
    out[1]= (65280 & temp)>>8;
    out[2]= (16711680 & temp)>>16;
    std::cout<<std::bitset<8>(out[0])<<std::endl;
    std::cout<<std::bitset<8>(out[1])<<std::endl;
    std::cout<<std::bitset<8>(out[2])<<std::endl;
    relayConfLoadEEPROM(out);
}
void relayConfLoadEEPROM(uint8_t * out){
    uint32_t temp = out[0] | out[1] << 8 | out[2] << 16;
    short r1 = temp & 31;
    short r2 = (temp & 992) >> 5;
    short r3 = (temp & 31744) >> 10;
    short r4 = (temp & 1015808) >> 15;
    relayconf.r1=inttoRelayMap(r1);
    relayconf.r2=inttoRelayMap(r2);
    relayconf.r3=inttoRelayMap(r3);
    relayconf.r4=inttoRelayMap(r4);
    std::cout<<std::bitset<8>(inttoRelayMap(r1))<<std::endl;
    std::cout<<std::bitset<8>(inttoRelayMap(r2))<<std::endl;
    std::cout<<std::bitset<8>(inttoRelayMap(r3))<<std::endl;
    std::cout<<std::bitset<8>(inttoRelayMap(r4))<<std::endl;
}
int main(int argc,char **argv){
    //uint8_t a = 0b10000001;
    //std::cout<<relayMaptoInt(a)<<std::endl;
    // for(int i=0;i<29;i++){
    //     uint8_t z =inttoRelayMap(i);
    //     int second = relayMaptoInt(z);
    //     std::cout<<second<<std::endl;
    //     std::cout<< std::bitset<8>(z) <<std::endl;
    // }
    relayconf.r1=0b00000011;
    // std::cout<<std::bitset<8>(relayconf.r1)<<std::endl;
    relayconf.r2=0b00000110;
    // std::cout<<std::bitset<8>(relayconf.r2)<<std::endl;
    relayconf.r3=0b00001100;
    relayconf.r4=0b00011000;
    relayConfDumpEEPROM();
    return 0;
}