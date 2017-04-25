import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Trust} from "./trust.model";
import {TrustService} from "./trust.service";
@Injectable()
export class TrustPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private trustService: TrustService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.trustService.find(id).subscribe(trust => {
				this.trustModalRef(component, trust);
			});
		} else {
			return this.trustModalRef(component, new Trust());
		}
	}

	trustModalRef(component: Component, trust: Trust): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.trust = trust;
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
