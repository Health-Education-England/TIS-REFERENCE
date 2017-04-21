import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FundingIssue} from "./funding-issue.model";
import {FundingIssueService} from "./funding-issue.service";
@Injectable()
export class FundingIssuePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private fundingIssueService: FundingIssueService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.fundingIssueService.find(id).subscribe(fundingIssue => {
				this.fundingIssueModalRef(component, fundingIssue);
			});
		} else {
			return this.fundingIssueModalRef(component, new FundingIssue());
		}
	}

	fundingIssueModalRef(component: Component, fundingIssue: FundingIssue): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.fundingIssue = fundingIssue;
		modalRef.result.then(result => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
