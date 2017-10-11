function  meinv()
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes he
for i = 1:15
    a = 30 + i;
    a = num2str(a);
    b = '.jpg';
    a0 = strcat(a,b);
    c = imread(a0);
    c = imresize(c,[200 150]);
    imwrite(c,strcat(a,'.jpg'));
end

