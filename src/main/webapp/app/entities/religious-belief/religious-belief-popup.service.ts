import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {ReligiousBelief} from "./religious-belief.model";
import {ReligiousBeliefService} from "./religious-belief.service";
@Injectable()
export class ReligiousBeliefPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private religiousBeliefService: ReligiousBeliefService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.religiousBeliefService.find(id).subscribe(religiousBelief => {
				this.religiousBeliefModalRef(component, religiousBelief);
			});
		} else {
			return this.religiousBeliefModalRef(component, new ReligiousBelief());
		}
	}

	religiousBeliefModalRef(component: Component, religiousBelief: ReligiousBelief): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.religiousBelief = religiousBelief;
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
