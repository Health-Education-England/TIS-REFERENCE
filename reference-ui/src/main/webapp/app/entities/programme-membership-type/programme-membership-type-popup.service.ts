import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {ProgrammeMembershipType} from "./programme-membership-type.model";
import {ProgrammeMembershipTypeService} from "./programme-membership-type.service";
@Injectable()
export class ProgrammeMembershipTypePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private programmeMembershipTypeService: ProgrammeMembershipTypeService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.programmeMembershipTypeService.find(id).subscribe(programmeMembershipType => {
				this.programmeMembershipTypeModalRef(component, programmeMembershipType);
			});
		} else {
			return this.programmeMembershipTypeModalRef(component, new ProgrammeMembershipType());
		}
	}

	programmeMembershipTypeModalRef(component: Component, programmeMembershipType: ProgrammeMembershipType): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.programmeMembershipType = programmeMembershipType;
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
