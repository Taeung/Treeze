package freemind.controller;

public class FreemindLectureManager {
	 
    private static LectureInfo instance;
    // �Ǵ� private static Singleton instance = new Singleton();���� �̸� ����
 
    // ������ �������� Ű���尡 public�� �ƴ� private�̴�.
    // ���� Ŭ���� ���ο����� ��밡���� �����ڰ� �ȴ�.
    private FreemindLectureManager() {  }
 
    // public : ��𼭵� ������ �����ϴ�.
    // static : Singleton �ν��Ͻ��� �������� �ʾƵ� ��밡���� �޼ҵ�
    // �ν��Ͻ��� �޴� getter�޼ҵ�
    // ��ȯ�� Singleton���� �Ѵ�.
    public static LectureInfo getInstance() {
 
        // �ν��Ͻ��� null�̸� �������� �ʾҴٴ� �ǹ�
        // �� �ѹ��� �����ϴ� �̱����̶��
        // ������ �ν��Ͻ��� �̸� �����صΰ� if�� �����Ѵ�.
        if(instance == null) {
 
            // �� if���� true��(�ν��Ͻ��� ���ٸ�) �����Ѵ�. �޸� �Ҵ�.
            instance = new LectureInfo();
 
        } // end if
 
        // false��� �̹� �����ƴٴ� �ǹ�.
        // �̹� �ִ�, ���� �����ߴ� �̱��� �ν��Ͻ��� ��ȯ���ش�.
        return instance;
 
    } // end getInstance()
} // end class
