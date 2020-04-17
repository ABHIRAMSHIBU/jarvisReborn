#include<iostream>
#include<bitset>
#include<cmath>
short relayMaptoInt(uint8_t input){
    uint8_t out=1;
    uint8_t val=0b10000000;
    if(input==0b00000000){
        return 0;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & val)!=val){
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
    uint8_t val=0b10000000;
    if(input==0){
        return 0b00000000;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & val)!=val){
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
int main(int argc,char **argv){
    //uint8_t a = 0b10000001;
    //std::cout<<relayMaptoInt(a)<<std::endl;
    for(int i=0;i<29;i++){
        uint8_t z =inttoRelayMap(i);
        int second = relayMaptoInt(z);
        std::cout<<second<<std::endl;
        std::cout<< std::bitset<8>(z) <<std::endl;
    }
    return 0;
}