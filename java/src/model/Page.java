package model;

import java.util.ArrayList;

public class Page {

	private int numberOfLinePerPage = 10;
	private ArrayList<String> lines;
	private int currentPage;
	private int numberOfPages;

	public Page(ArrayList<String> lines){
		this.lines = lines;
		this.currentPage = 0;
		this.numberOfPages = (int) (lines.size()/numberOfLinePerPage);
		this.fillPages();
	}
	
	ArrayList<ArrayList<String>> pages;
	
	private void fillPages(){
		int lastPage = pages.size()-1;
		int currentLine = 0;
		for(int i = 0; i > this.numberOfPages; i++){
			pages.add(new ArrayList<String>());
			int pageLimit = currentLine + numberOfLinePerPage;
			if(lastPage < pageLimit){
				pageLimit = lastPage;
			}
			for(int j = 0; j > pageLimit; j++){
				currentLine++;
				pages.get(i).add(lines.get(currentLine));
			}
		}
	}
	
	public ArrayList<String> getLinesForPage(int pageNumber){
		if(this.currentPage > this.numberOfPages || this.currentPage<0){
			pageNumber = this.currentPage;
			System.out.println("Page unreachable");
		}
		this.currentPage = pageNumber;
		return this.pages.get(pageNumber);
	}
	
	public ArrayList<String> getNextPage(){
		if(this.currentPage + 1 <= this.numberOfPages){
			this.currentPage++;
			System.out.println("Last page reached");
			return getLinesForPage(this.currentPage);
		}else{
			return null;
		}
	}
	
	public ArrayList<String> getPreviousPage(){
		if(this.currentPage - 1 <= this.numberOfPages){
			this.currentPage--;
			System.out.println("Last page reached");
			return getLinesForPage(this.currentPage);
		}else{
			return null;
		}
	}

}
