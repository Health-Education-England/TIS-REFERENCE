import {Injectable, Component} from "@angular/core";
import {Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Title} from "./title.model";
import {TitleService} from "./title.service";
@Injectable()
export class TitlePopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private titleService: TitleService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.titleService.find(id).subscribe(title => {
				this.titleModalRef(component, title);
			});
		} else {
			return this.titleModalRef(component, new Title());
		}
	}

	titleModalRef(component: Component, title: Title): NgbModalRef {
		let modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.title = title;
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
