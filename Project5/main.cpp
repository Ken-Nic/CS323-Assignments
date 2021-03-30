#include<string>
#include<iostream>
#include<fstream>
using namespace std;

class Image 
{
    private: 
    int numRows,numCols,minVal,maxVal,power2Size;
    int ** imgAry;


    public:
    
    // Constructor
    Image(int row,int column,int min, int max)
    {
        this->numRows = row;
        this->numCols = column;
        this->minVal = min;
        this->maxVal = max;
    }

    // Getters & Setters
    int ** getArray()
    {
        return this->imgAry;
    }

    int getPower2Size ()
    {
        return this->power2Size;
    }


    // Methods
    int computePower2(int r,int c,ofstream & output)
    {
        int size = max(r,c);
        int power2 = 2;

        while(size > power2)
        {
            power2 *= 2;
        }
        output << "ComputePower2 returned: " << power2 <<"\n";
        this->power2Size = power2;
        allocateAry();
        return power2;
    }

    void allocateAry()
    {
        this->imgAry = new int * [this->power2Size];
        for(int i = 0; i< this->power2Size; i++)
        {
            imgAry[i] = new int[this->power2Size];
        }
        zero2DAry();
    }

    void loadImage(ifstream & input,ofstream & output)
    {
        int data;
        output<<"\n----Printing ImgArray w/ padding----\n";
        for(int i = 0; i < this->power2Size; i++)
        {
            
            for(int j = 0; j < this->power2Size; j++)
            {
                input >> data;
                this->imgAry[i][j]=data;
                output << this->imgAry[i][j] <<" ";
               
            }
            output << "\n";
        }
    }

    void zero2DAry()
    {
        for(int i = 0; i < this->power2Size; i++)
        {
           for(int j = 0; j < this->power2Size; j++)
           {
               this->imgAry[i][j] = 0;
           }
        }
    }
};

class QtTreeNode
{
    private:
    int color,upperR,upperC,size;
    QtTreeNode * NWkid = NULL;
    QtTreeNode * NEkid = NULL;
    QtTreeNode * SWkid = NULL;
    QtTreeNode * SEkid = NULL;

    public:

    // Constructor
    QtTreeNode(int c,int uR,int uC, int s, QtTreeNode * NW,QtTreeNode * NE,QtTreeNode * SW,QtTreeNode * SE)
    {
        this->color = c;
        this->upperR = uR;
        this->upperC = uC,
        this->size = s;
        this->NWkid = NW;
        this->NEkid = NE;
        this->SWkid = SW;
        this->SEkid = SE;
    }

    // Method
    void printQtNode(ofstream & output)
    {
        (this->NWkid != NULL) ? output << this->color << "," << this->upperR << "," << this->upperC << "," << this->NWkid->color << this->NEkid->color << this->SWkid->color << this->SEkid->color <<"\n" : output << this->color << "," << this->upperR << "," << this->upperC << "," <<"NULL,NULL,NULL,NULL\n";
    }

    // Getters & Setters
    int getColor()
    {
        return this->color;
    }

    QtTreeNode * getNW()
    {
        return this->NWkid;
    }

    QtTreeNode * getNE()
    {
        return this->NEkid;
    }

    QtTreeNode * getSW()
    {
        return this->SWkid;
    }

    QtTreeNode * getSE()
    {
        return this->SEkid;
    }

    friend class QuadTree;
};

class QuadTree 
{
    private:
    QtTreeNode * QtRoot;

    public:

    QtTreeNode * buildQuadTree (int ** imgAry,int upR, int upC, int s, ofstream & output)
    {
        QtTreeNode * newQtNode = new QtTreeNode(-1,upR,upC,s,NULL,NULL,NULL,NULL);
        newQtNode->printQtNode(output);


        if(s == 1)
        {
            newQtNode->color = imgAry[upR][upC];
        }
        else
        {
            int halfSize = s/2;
            newQtNode->NWkid = buildQuadTree(imgAry,upR,upC,halfSize,output);
            newQtNode->NEkid = buildQuadTree(imgAry,upR,upC + halfSize,halfSize,output);
            newQtNode->SWkid = buildQuadTree(imgAry,upR + halfSize,upC,halfSize,output);
            newQtNode->SEkid = buildQuadTree(imgAry,upR + halfSize,upC + halfSize,halfSize,output);

            int sumColor = newQtNode->NWkid->color + newQtNode->NEkid->color + newQtNode->SWkid->color + newQtNode->SEkid->color;

            if(sumColor == 0)
            {
                newQtNode->color = 0;
                newQtNode->NWkid = NULL;
                newQtNode->NEkid = NULL;
                newQtNode->SWkid = NULL;
                newQtNode->SEkid = NULL;
            }
            else if (sumColor == 4)
            {
                newQtNode->color = 1;
                newQtNode->NWkid = NULL;
                newQtNode->NEkid = NULL;
                newQtNode->SWkid = NULL;
                newQtNode->SEkid = NULL;
            }
            else
            {
                newQtNode->color = 5;
            }
        }
        return newQtNode;    
    }

    bool isLeaf(QtTreeNode * n)
    {
        if(n->getNW() == NULL && n->getNE() == NULL && n->getSE() == NULL && n->getNW() == NULL)
        {
            return true;
        }
        return false;
    }

    void preOrder(QtTreeNode * Qt, ofstream & outfile)
    {
        if(this->isLeaf(Qt))
        {
            Qt->printQtNode(outfile);
        }
        else
        {
            Qt->printQtNode(outfile);
            preOrder(Qt->NWkid,outfile);
            preOrder(Qt->NEkid,outfile);
            preOrder(Qt->SWkid,outfile);
            preOrder(Qt->SEkid,outfile);
        }
    }

    void postOrder(QtTreeNode * Qt, ofstream & outfile)
    {
         if(this->isLeaf(Qt))
        {
            Qt->printQtNode(outfile);
        }
        else
        {
            postOrder(Qt->NWkid,outfile);
            postOrder(Qt->NEkid,outfile);
            postOrder(Qt->SWkid,outfile);
            postOrder(Qt->SEkid,outfile);
            Qt->printQtNode(outfile);
        }

    }

    // Getters & Setters
    void setRoot(QtTreeNode * n)
    {
        this->QtRoot = n;
    }

    QtTreeNode * getRoot()
    {
        return this->QtRoot;
    }
};

int main(int argc, const char * argv[]) 
{
    ifstream input;
    ofstream output1,output2;

    input.open(argv[1]);
    output1.open(argv[2]);
    output2.open(argv[3]);

    int rows,columns,min,max;
    input >> rows >> columns >> min >> max;
    Image * quadImage = new Image(rows,columns,min,max);
    quadImage->computePower2(rows,columns,output2);
    quadImage->loadImage(input,output2);

    QuadTree * Qtree = new QuadTree();
    output2 << "\n ----printing treeNodes ---- \n";
    Qtree->setRoot(Qtree->buildQuadTree(quadImage->getArray(),0,0,quadImage->getPower2Size(),output2));
    output1 <<"----Printing preOrder----\n";
    Qtree->preOrder(Qtree->getRoot(),output1);
    output1 <<"\n----Printing postOrder----\n";
    Qtree->postOrder(Qtree->getRoot(),output1);

    input.close();
    output1.close();
    output2.close();
}