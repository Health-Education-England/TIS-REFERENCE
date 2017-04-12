import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Settled} from "./settled.model";
import {SettledService} from "./settled.service";
@Injectable()
export class SettledPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private settledService: SettledService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.settledService.find(id).subscribe(settled => {
				this.settledModalRef(component, settled);
			});
		} else {
			return this.settledModalRef(component, new Settled());
		}
	}

	settledModalRef(component: Component, settled: Settled): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.settled = settled;
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
