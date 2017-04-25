import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {FundingType} from "./funding-type.model";
import {FundingTypeService} from "./funding-type.service";
@Injectable()
export class FundingTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private fundingTypeService: FundingTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.fundingTypeService.find(id).subscribe(fundingType => {
				this.fundingTypeModalRef(component, fundingType);
			});
		} else {
			return this.fundingTypeModalRef(component, new FundingType());
		}
	}

	fundingTypeModalRef(component: Component, fundingType: FundingType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.fundingType = fundingType;
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
